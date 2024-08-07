package org.metadatacenter.nih.ingestor.converter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.metadatacenter.artifacts.model.core.FieldSchemaArtifact;
import org.metadatacenter.artifacts.model.core.fields.TemporalGranularity;
import org.metadatacenter.artifacts.model.core.fields.XsdTemporalDatatype;
import org.metadatacenter.artifacts.model.core.Version;
import org.metadatacenter.artifacts.model.core.ListField.ListFieldBuilder;
import org.metadatacenter.artifacts.model.core.NumericField.NumericFieldBuilder;
import org.metadatacenter.artifacts.model.core.TemporalField.TemporalFieldBuilder;
import org.metadatacenter.artifacts.model.core.TextField.TextFieldBuilder;
import org.metadatacenter.artifacts.model.renderer.JsonSchemaArtifactRenderer;
import org.metadatacenter.nih.ingestor.constants.DataTypes;
import org.metadatacenter.nih.ingestor.constants.JsonDatePrecisions;
import org.metadatacenter.nih.ingestor.constants.NIHJsonKeys;
import org.metadatacenter.nih.ingestor.exceptions.DesignationNotFoundException;
import org.metadatacenter.nih.ingestor.exceptions.InvalidDatePrecisionException;
import org.metadatacenter.nih.ingestor.exceptions.InvalidJsonPathException;
import org.metadatacenter.nih.ingestor.exceptions.UnsupportedDataTypeException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

public class Converter {
    ObjectMapper mapper = new ObjectMapper();
    ObjectReader stringListReader = mapper.readerFor(new TypeReference<List<String>>() {});
    JsonSchemaArtifactRenderer jsonSchemaArtifactRenderer = new JsonSchemaArtifactRenderer();

    /*
    Read a json file into a JsonNode from its filepath.
    This is Jackson's tree representation of the Json document.
    The NIH CDE repository exports as a single .json file containing a list
    of CDE objects, i.e.,
        [ {CDE_1}, {CDE_2}, ..., {CDE_N} ]
    The "head" node of the JsonNode tree contains a list of JsonNodes,
    each specifying a full CDE.
     */
    private JsonNode readJsonToTree(String fileName) throws IOException {
        String jsonString = new String(Files.readAllBytes(Paths.get(fileName)));
        return mapper.readTree(jsonString);
    }

    /*
    Strings read in from the .json include the start and end quotation marks.
    This removes those quotation marks.
     */
    protected String nodeToCleanedString(JsonNode node) {
        // delete the leading and ending double quote characters
        // also ensure that those are also the leading and ending characters
        String verbatimString = node.toString();
        if (verbatimString.startsWith("\"") && verbatimString.endsWith("\"")) {
            return verbatimString.substring(1, verbatimString.length()-1);
        } else {
            return verbatimString;
        }
    }

    /*
    Builds a FieldSchemaArtifact using the cedar-artifact-library using the provided information.
    Select the appropriate builder from the cedar-artifact-library for the
    response datatype specified by the CDE.
     */
    private FieldSchemaArtifact makeCedarArtifact(String datatype,
                                                  CDEConstraints constraints,
                                                  String tinyId,
                                                  Version version,
                                                  Optional<String> definition,
                                                  String preferredLabel,
                                                  List<String> alternateLabels,
                                                  String name)
            throws UnsupportedDataTypeException {
        switch (datatype) {
            case DataTypes.FILE:
            case DataTypes.EXTERNALLYDEFINED:
            case DataTypes.TEXT:
                TextFieldBuilder textBuilder = new TextFieldBuilder();
                if (constraints.hasMinLength()) {
                    textBuilder.withMinLength(constraints.getMinLength());
                }
                if (constraints.hasMaxLength()) {
                    textBuilder.withMaxLength(constraints.getMaxLength());
                }
                if (definition.isPresent()) {
                    textBuilder = textBuilder.withDescription(definition.get());
                }
                return textBuilder.withName(name).
                        withIdentifier(tinyId).
                        withPreferredLabel(preferredLabel).
                        withAlternateLabels(alternateLabels).
                        build();
            case DataTypes.NUMBER:
                NumericFieldBuilder numBuilder = new NumericFieldBuilder();
                if (constraints.hasNumericPrecision()) {
                    numBuilder.withDecimalPlaces(constraints.getNumericPrecision());
                }
                if (constraints.hasNumericType()) {
                    numBuilder.withNumericType(constraints.getNumericType());
                }
                if (constraints.hasMinValue()) {
                    numBuilder.withMinValue(constraints.getMinValue());
                }
                if (constraints.hasMaxValue()) {
                    numBuilder.withMaxValue(constraints.getMaxValue());
                }
                if (definition.isPresent()) {
                    numBuilder = numBuilder.withDescription(definition.get());
                }
                return numBuilder.withName(name).
                        withIdentifier(tinyId).
                        withPreferredLabel(preferredLabel).
                        withAlternateLabels(alternateLabels).
                        build();
            case DataTypes.DATE:
                TemporalFieldBuilder dateBuilder = new TemporalFieldBuilder();
                if (constraints.hasDatePrecision()) {
                    String datePrecision = constraints.getDatePrecision();
                    if (datePrecision.equals(JsonDatePrecisions.MINUTE)) {
                        dateBuilder.withTemporalGranularity(TemporalGranularity.MINUTE).
                            withTemporalType(XsdTemporalDatatype.DATETIME);
                    } else {
                        dateBuilder.withTemporalGranularity(TemporalGranularity.DAY).
                            withTemporalType(XsdTemporalDatatype.DATE);
                    }
                } else {
                    // NIH CDE Repository default is Day
                    dateBuilder.withTemporalGranularity(TemporalGranularity.DAY).
                        withTemporalType(XsdTemporalDatatype.DATE);
                }
                if (definition.isPresent()) {
                    dateBuilder = dateBuilder.withDescription(definition.get());
                }
                return dateBuilder.withName(name).
                        withIdentifier(tinyId).
                        withPreferredLabel(preferredLabel).
                        withAlternateLabels(alternateLabels).
                        build();
            case DataTypes.TIME:
                // assumed minute. nothing in the CDE indicates time granularity.
                return new TemporalFieldBuilder().
                        withTemporalType(XsdTemporalDatatype.TIME).
                        withTemporalGranularity(TemporalGranularity.MINUTE).
                        build();
            case DataTypes.VALUELIST:
                ListFieldBuilder listFieldBuilder = new ListFieldBuilder();
                if (constraints.hasPermissibleValues()) {
                    for (String value: constraints.getPermissibleValues()) {
                        listFieldBuilder.withOption(value);
                    }
                }
                listFieldBuilder.withIsMultiple(false);
                return listFieldBuilder.build();
            default:
                // will not be reached if valid data type is used
                throw new UnsupportedDataTypeException(datatype);
        }
    }

    /*
    Obtain the JsonNode corresponding to a sequence of keys that describes how
    a JSON object is structured.
    Examples:
    For a JSON object that looks like the following, the path "key1/key2/key3"
    will return the node containing "value1"
        {"key1": {"key2": {"key3": "value1"}}}

    For a JSON object that looks like the following, the path "key1/1/key3/"
    will return the node containing "value2"
        {"key1": [{"key2": "value1"}, {"key3": "value2"}]}

    An assumption here is that the path must point to a non-empty Node.
    Inspecting the exported NIH CDEs shows that there are no "key": null pairs.
     */
    private JsonNode getNodeAtJsonPath(JsonNode currentNode, String path) throws InvalidJsonPathException {
        String[] keys = path.split("/");
        for (String key: keys) {
            if (currentNode.isArray()) {
                currentNode = currentNode.get(Integer.parseInt(key));
            } else {
                currentNode = currentNode.get(key);
            }
            // if the new current node is null, the index or key was invalid
            if (currentNode == null) {
                throw new InvalidJsonPathException(path);
            }
        }
        return currentNode;
    }

    /*
    Make sure an expected node has a value. JsonNode.has() will allow for explicitly
    entered null values in the JSON. The assumption here is that a path must point to
    a Node. Inspecting the exported NIH CDEs shows that there are no {"key": null} pairs.
     */
    protected void checkNodeHasNonNull(JsonNode node, String key, String path) throws InvalidJsonPathException {
        if (!node.has(key)) {
            throw new InvalidJsonPathException(path);
        }
    }

    /*
    Same as above but for array Nodes.
     */
    protected void checkNodeHasNonNull(JsonNode node, int key, String path) throws InvalidJsonPathException {
        if (!node.has(key)) {
            throw new InvalidJsonPathException(path);
        }
    }

    /*
    Returns the value at {"valueDomain": {"datatype": "value", ...}}
     */
    protected String getDatatype(JsonNode headNode) throws InvalidJsonPathException {
        checkNodeHasNonNull(headNode, NIHJsonKeys.VALUEDOMAIN, NIHJsonKeys.VALUEDOMAIN);
        JsonNode currentNode = headNode.get(NIHJsonKeys.VALUEDOMAIN);
        checkNodeHasNonNull(currentNode, NIHJsonKeys.DATATYPE,
                String.format("%s/%s", NIHJsonKeys.VALUEDOMAIN, NIHJsonKeys.DATATYPE));
        return nodeToCleanedString(currentNode.get(NIHJsonKeys.DATATYPE));
    }

    /*
    Returns the value at {"tinyId": "value"}
     */
    protected String getTinyId(JsonNode headNode) throws InvalidJsonPathException {
        checkNodeHasNonNull(headNode, NIHJsonKeys.TINYID, NIHJsonKeys.TINYID);
        return nodeToCleanedString(headNode.get(NIHJsonKeys.TINYID));
    }

    /*
    Returns the value at {"__v": value}
    The __v value is just an integer, so the minor and patch numbers are taken to be 0.
     */
    protected Version getVersion(JsonNode headNode) throws InvalidJsonPathException {
        checkNodeHasNonNull(headNode, NIHJsonKeys.VERSION, NIHJsonKeys.VERSION);
        int version = headNode.get(NIHJsonKeys.VERSION).intValue();
        return new Version(version, 0, 0);
    }

    /*
    Extract list of permissible values to translate into a Value List.
    The values extracted are placed into a string array. Returns values
    "value_i" at:
    {
        "valueDomain": {
            "datatype": "Value List",
            "permissibleValues": [
                {
                    "permissibleValue": "some_label_1",
                    "valueMeaningName": "value_1"
                },
                {
                    "permissibleValue": "some_label_2",
                    "valueMeaningName": "value_2"
                }
            ]
        }
    }
    Note that the "permissibleValue" field is usually an uninformative label if it is
    accompanied by "valueMeaningName", so we label it with both the original value
    and its name. However, "permissibleValue" should be chosen if it is accompanied by
    "valueMeaningDefinition" instead.
     */
    protected ArrayList<String> getPermissibleValues(JsonNode headNode) throws InvalidJsonPathException {
        checkNodeHasNonNull(headNode, NIHJsonKeys.VALUEDOMAIN, NIHJsonKeys.VALUEDOMAIN);
        JsonNode currentNode = headNode.get(NIHJsonKeys.VALUEDOMAIN);
        checkNodeHasNonNull(currentNode, NIHJsonKeys.PERMISSIBLEVALUES,
                String.format("%s/%s/", NIHJsonKeys.VALUEDOMAIN, NIHJsonKeys.PERMISSIBLEVALUES));
        currentNode = currentNode.get(NIHJsonKeys.PERMISSIBLEVALUES);

        HashSet<String> permissibleValuesSet = new HashSet<>();
        ArrayList<String> permissibleValues = new ArrayList<>();
        if (!currentNode.isEmpty()) {
            for (int i = 0; i < currentNode.size(); i++) {
                JsonNode node = currentNode.get(i);
                if (node.hasNonNull(NIHJsonKeys.VALUEMEANINGNAME)) {
                    String permissibleValue = String.format("%s - %s",
                            nodeToCleanedString(node.get(NIHJsonKeys.PERMISSIBLEVALUE)),
                            nodeToCleanedString(node.get(NIHJsonKeys.VALUEMEANINGNAME)));
                    if (!permissibleValuesSet.contains(permissibleValue)) {
                        permissibleValuesSet.add(permissibleValue);
                        permissibleValues.add(permissibleValue);
                    }
                } else {
                    checkNodeHasNonNull(node, NIHJsonKeys.PERMISSIBLEVALUE,
                            String.format("%s/%s/%d/%s", NIHJsonKeys.VALUEDOMAIN, NIHJsonKeys.PERMISSIBLEVALUES,
                                    i, NIHJsonKeys.PERMISSIBLEVALUE));
                    String permissibleValue = nodeToCleanedString(node.get(NIHJsonKeys.PERMISSIBLEVALUE));
                    if (!permissibleValuesSet.contains(permissibleValue)) {
                        permissibleValuesSet.add(permissibleValue);
                        permissibleValues.add(permissibleValue);
                    }
                }
            }
        }
        return permissibleValues;
    }

    /*
    Returns the value of a NIH CDE at {"definitions": [{"definition": "value", "tags": []}], ...}
    Inspecting the NIH CDE JSON file shows that there is at most one definition value despite
    the use of an array. There is sometimes no definition, in which case the array is empty.
     */
    protected Optional<String> getDefinition(JsonNode headNode) throws InvalidJsonPathException {
        checkNodeHasNonNull(headNode, NIHJsonKeys.DEFINITIONS, NIHJsonKeys.DEFINITIONS);
        JsonNode currentNode = headNode.get(NIHJsonKeys.DEFINITIONS);
        if (currentNode.isEmpty()) {
            return Optional.empty();
        } else {
            // check only first index in the array
            checkNodeHasNonNull(currentNode, 0, String.format("%s/%d", NIHJsonKeys.DEFINITION, 0));
            currentNode = currentNode.get(0);
            checkNodeHasNonNull(currentNode, NIHJsonKeys.DEFINITION,
                    String.format("%s/%d/%s", NIHJsonKeys.DEFINITIONS, 0, NIHJsonKeys.DEFINITION));
            return Optional.of(nodeToCleanedString(currentNode.get(NIHJsonKeys.DEFINITION)));
        }
    }

    /*
    Designations are the labels for the CDE. Sometimes, one of the designations
    is tagged as the "Preferred Question Text" and we take this to be the preferred
    label in CEDAR.
    The designation labels are found as "value_i" in the JSON object as follows:
    {
        "designations": [
            {
                "designation": "value_i",
                "tags": []
            },
            {
                "designation": "value_i",
                "tags": ["Some Other Text"]
            },
            {
                "designation": "value_i",
                "tags": ["Preferred Question Text"]
            }
        ]
    }
    Inspecting the NIH CDE JSON doc shows that some designations have multiple tags.
    We will need to go through them to identify the preferred label.
    There are also no CDE entries with zero designations. If this is the case,
    we'll throw an error so the user knows to inspect the CDE.
     */
    protected CDEDesignations getDesignations(JsonNode headNode)
            throws IOException, InvalidJsonPathException, DesignationNotFoundException {
        checkNodeHasNonNull(headNode, NIHJsonKeys.DESIGNATIONS, NIHJsonKeys.DESIGNATIONS);
        JsonNode designationsNode = headNode.get(NIHJsonKeys.DESIGNATIONS);
        Optional<String> preferredLabel = Optional.empty();
        HashSet<String> alternateLabelsSet = new HashSet<>();
        ArrayList<String> alternateLabels = new ArrayList<>();
        if (designationsNode.isEmpty()) {
            throw new DesignationNotFoundException();
        } else {
            for (int i = 0; i < designationsNode.size(); i++) {
                JsonNode node = designationsNode.get(i);
                checkNodeHasNonNull(node, NIHJsonKeys.TAGS,
                        String.format("%s/%d/%s", NIHJsonKeys.DESIGNATIONS, i, NIHJsonKeys.TAGS));
                JsonNode tagsNode = node.get(NIHJsonKeys.TAGS);

                checkNodeHasNonNull(node, NIHJsonKeys.DESIGNATION,
                        String.format("%s/%d/%s", NIHJsonKeys.DESIGNATIONS, i, NIHJsonKeys.DESIGNATION));
                JsonNode designationNode = node.get(NIHJsonKeys.DESIGNATION);
                if (tagsNode.isEmpty()) {
                    String alternateLabel = nodeToCleanedString(designationNode);
                    if (!alternateLabelsSet.contains(alternateLabel)) {
                        alternateLabels.add(alternateLabel);
                        alternateLabelsSet.add(alternateLabel);
                    }
                } else {
                    List<String> tags = stringListReader.readValue(tagsNode);
                    if (tags.contains(NIHJsonKeys.PREFERREDQUESTIONTEXT)) {
                        preferredLabel = Optional.of(nodeToCleanedString(designationNode));
                    } else {
                        String alternateLabel = nodeToCleanedString(designationNode);
                        if (!alternateLabelsSet.contains(alternateLabel)) {
                            alternateLabels.add(alternateLabel);
                            alternateLabelsSet.add(alternateLabel);
                        }
                    }
                }
            }
        }
        // just pick the first label to use as a preferred label if no preference was indicated
        if (preferredLabel.isEmpty()) {
            preferredLabel = Optional.of(alternateLabels.get(0));
        }
        return new CDEDesignations(preferredLabel.get(), alternateLabels);
    }

    /*
    How to populate the name field for CEDAR. Choose the first alternate label or
    default to the preferred label. It looks like the preferred label is rarely
    used as the full name of the CDE in the NIH CDE repository.
     */
    protected String getName(CDEDesignations designations) {
        return designations.getAlternateLabels().isEmpty() ? designations.getPreferredLabel() : designations.getAlternateLabels().get(0);
    }

    /*
    Parse minLength and maxLength constraints if they are present in the CDE.
     */
    protected CDEConstraints getTextConstraints(JsonNode headNode) {
        CDEConstraints.CDEConstraintsBuilder builder = new CDEConstraints.CDEConstraintsBuilder();
        JsonNode currentNode = headNode.get(NIHJsonKeys.VALUEDOMAIN);
        if (!currentNode.has(NIHJsonKeys.DATATYPETEXT)) {
            return builder.build();
        }
        currentNode = currentNode.get(NIHJsonKeys.DATATYPETEXT);

        if (currentNode.has(NIHJsonKeys.MINLENGTH)) {
            builder = builder.withMinLength(currentNode.get(NIHJsonKeys.MINLENGTH).intValue());
        }
        if (currentNode.has(NIHJsonKeys.MAXLENGTH)) {
            builder = builder.withMaxLength(currentNode.get(NIHJsonKeys.MAXLENGTH).intValue());
        }
        return builder.build();
    }

    /*
    Parse minValue, maxValue, and precision if they are present in the CDE.
     */
    protected CDEConstraints getNumberConstraints(JsonNode headNode) {
        CDEConstraints.CDEConstraintsBuilder builder = new CDEConstraints.CDEConstraintsBuilder();
        JsonNode currentNode = headNode.get(NIHJsonKeys.VALUEDOMAIN);
        if (!currentNode.has(NIHJsonKeys.DATATYPENUMBER)) {
            return builder.build();
        }
        currentNode = currentNode.get(NIHJsonKeys.DATATYPENUMBER);

        // if precision is specified and it is not zero, the min/max values must be double
        if (currentNode.has(NIHJsonKeys.PRECISION)) {
            Integer precision = currentNode.get(NIHJsonKeys.PRECISION).intValue();
            builder = builder.withNumericPrecision(precision);
        }
        if (currentNode.has(NIHJsonKeys.MINVALUE)) {
            Number minValue = currentNode.get(NIHJsonKeys.MINVALUE).numberValue();
            builder.withMinValue(minValue);
        }
        if (currentNode.has(NIHJsonKeys.MAXVALUE)) {
            Number maxValue = currentNode.get(NIHJsonKeys.MAXVALUE).numberValue();
            builder.withMaxValue(maxValue);
        }
        return builder.build();
    }

    /*
    Parse the precision of the date specification if present in the CDE.
     */
    protected CDEConstraints getDateConstraints(JsonNode headNode) throws InvalidDatePrecisionException {
        CDEConstraints.CDEConstraintsBuilder builder = new CDEConstraints.CDEConstraintsBuilder();
        JsonNode currentNode = headNode.get(NIHJsonKeys.VALUEDOMAIN);
        if (!currentNode.has(NIHJsonKeys.DATATYPEDATE)) {
            return builder.build();
        }
        currentNode = currentNode.get(NIHJsonKeys.DATATYPEDATE);

        if (currentNode.has(NIHJsonKeys.PRECISION)) {
            String precision = nodeToCleanedString(currentNode.get(NIHJsonKeys.PRECISION));
            if (!JsonDatePrecisions.ALLOWEDDATEPRECISIONS.contains(precision)) {
                throw new InvalidDatePrecisionException(precision);
            }
            builder = builder.withDatePrecision(nodeToCleanedString(currentNode.get(NIHJsonKeys.PRECISION)));
        }
        return builder.build();
    }

    /*
    Parse the permissible value options specified by a CDE.
     */
    protected CDEConstraints getValueListConstraints(JsonNode headNode) throws InvalidJsonPathException {
        ArrayList<String> permissibleValues = getPermissibleValues(headNode);
        CDEConstraints.CDEConstraintsBuilder builder = new CDEConstraints.CDEConstraintsBuilder();
        if (!permissibleValues.isEmpty()) {
            builder = builder.withPermissibleValues(permissibleValues);
        }
        return builder.build();
    }

    /*
    Decide what constraints to read by the datatype of the CDE.
     */
    protected CDEConstraints getConstraints(JsonNode headNode, String datatype) throws InvalidJsonPathException, InvalidDatePrecisionException {
        switch (datatype) {
            case DataTypes.VALUELIST:
                return getValueListConstraints(headNode);
            case DataTypes.NUMBER:
                return getNumberConstraints(headNode);
            case DataTypes.TEXT:
                return getTextConstraints(headNode);
            case DataTypes.DATE:
                return getDateConstraints(headNode);
            default:
                return new CDEConstraints.CDEConstraintsBuilder().build();
        }
    }

    protected ObjectNode convertCDEToCedar(JsonNode cdeNode)
            throws IOException, InvalidJsonPathException, DesignationNotFoundException,
            InvalidDatePrecisionException, UnsupportedDataTypeException {
        String datatype = getDatatype(cdeNode);
        String tinyId = getTinyId(cdeNode);
        Version version = getVersion(cdeNode);
        CDEConstraints constraints = getConstraints(cdeNode, datatype);
        Optional<String> definition = getDefinition(cdeNode);
        CDEDesignations designations = getDesignations(cdeNode);
        String name = getName(designations);
        String preferredLabel = designations.getPreferredLabel();
        ArrayList<String> alternateLabels = designations.getAlternateLabels();

        FieldSchemaArtifact artifact = makeCedarArtifact(datatype, constraints,
                tinyId, version, definition, preferredLabel, alternateLabels, name);
        return jsonSchemaArtifactRenderer.renderFieldSchemaArtifact(artifact);
    }

    /*
    Read CDEs and populate important fields that can be converted into a CEDAR artifact.
     */
    public ArrayList<ObjectNode> convertCDEsToCEDAR(String filename)
            throws IOException, InvalidJsonPathException, DesignationNotFoundException,
            InvalidDatePrecisionException, UnsupportedDataTypeException {
        JsonNode jsonData = readJsonToTree(filename);
        ArrayList<ObjectNode> jsonRenders = new ArrayList<>();
        for (JsonNode cdeNode: jsonData) {
            ObjectNode rendering = convertCDEToCedar(cdeNode);
            jsonRenders.add(rendering);
        }
        return jsonRenders;
    }
}
