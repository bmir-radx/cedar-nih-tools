package org.metadatacenter.nih.ingestor.converter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.metadatacenter.artifacts.model.core.FieldSchemaArtifact;
import org.metadatacenter.artifacts.model.core.TemporalGranularity;
import org.metadatacenter.artifacts.model.core.TemporalType;
import org.metadatacenter.artifacts.model.core.builders.FieldSchemaArtifactBuilder;
import org.metadatacenter.artifacts.model.core.builders.ListFieldBuilder;
import org.metadatacenter.artifacts.model.core.builders.NumericFieldBuilder;
import org.metadatacenter.artifacts.model.core.builders.TemporalFieldBuilder;
import org.metadatacenter.artifacts.model.core.builders.TextFieldBuilder;
import org.metadatacenter.artifacts.model.renderer.JsonSchemaArtifactRenderer;
import org.metadatacenter.nih.ingestor.constants.DataTypes;
import org.metadatacenter.nih.ingestor.constants.JsonDatePrecisions;
import org.metadatacenter.nih.ingestor.constants.JsonKeys;
import org.metadatacenter.nih.ingestor.exceptions.DesignationNotFoundException;
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
     */
    private FieldSchemaArtifact makeCedarArtifact(String inputType,
                                                  CDEConstraints constraints,
                                                  String tinyId,
                                                  Optional<String> definition,
                                                  String preferredLabel,
                                                  List<String> alternateLabels,
                                                  String name)
            throws UnsupportedDataTypeException {

        FieldSchemaArtifactBuilder builder = chooseBuilderByDataType(inputType, constraints);
        if (definition.isPresent()) {
            builder = builder.withDescription(definition.get());
        }
        return builder.withName(name).
                withIdentifier(tinyId).
                withPreferredLabel(preferredLabel).
                withAlternateLabels(alternateLabels).
                build();
    }

    /*
    Select the appropriate builder from the cedar-artifact-library for the
    response datatype specified by the CDE.
     */
    private FieldSchemaArtifactBuilder chooseBuilderByDataType(String datatype, CDEConstraints constraints)
            throws UnsupportedDataTypeException {
        switch (datatype) {
            case DataTypes.FILE:
            case DataTypes.EXTERNALLYDEFINED:
            case DataTypes.TEXT:
                TextFieldBuilder textBuilder = FieldSchemaArtifact.textFieldBuilder();
                if (constraints.hasMinLength()) {
                    textBuilder = textBuilder.withMinLength(constraints.getMinLength());
                }
                if (constraints.hasMaxLength()) {
                    textBuilder = textBuilder.withMaxLength(constraints.getMaxLength());
                }
                return textBuilder;
            case DataTypes.NUMBER:
                NumericFieldBuilder numBuilder = FieldSchemaArtifact.numericFieldBuilder();
                if (constraints.hasNumericPrecision()) {
                    numBuilder = numBuilder.withDecimalPlaces(constraints.getNumericPrecision());
                }
                if (constraints.hasNumericType()) {
                    numBuilder = numBuilder.withNumericType(constraints.getNumericType());
                }
                if (constraints.hasMinValue()) {
                    numBuilder = numBuilder.withMinValue(constraints.getMinValue());
                }
                if (constraints.hasMaxValue()) {
                    numBuilder = numBuilder.withMaxValue(constraints.getMaxValue());
                }
                return numBuilder;
            case DataTypes.DATE:
                TemporalFieldBuilder dateBuilder = FieldSchemaArtifact.temporalFieldBuilder();
                if (constraints.hasDatePrecision()) {
                    String datePrecision = constraints.getDatePrecision();
                    if (datePrecision.equals(JsonDatePrecisions.MINUTE)) {
                        dateBuilder = dateBuilder.withTemporalGranularity(TemporalGranularity.MINUTE).
                            withTemporalType(TemporalType.DATETIME);
                    } else {
                        dateBuilder = dateBuilder.withTemporalGranularity(TemporalGranularity.DAY).
                            withTemporalType(TemporalType.DATE);
                    }
                } else {
                    // NIH CDE Repository default is Day
                    dateBuilder = dateBuilder.withTemporalGranularity(TemporalGranularity.DAY).
                        withTemporalType(TemporalType.DATE);
                }
                return dateBuilder;
            case DataTypes.TIME:
                // assumed minute. nothing in the CDE indicates time granularity.
                return FieldSchemaArtifact.temporalFieldBuilder().
                        withTemporalType(TemporalType.TIME).
                        withTemporalGranularity(TemporalGranularity.MINUTE);
            case DataTypes.VALUELIST:
                ListFieldBuilder listFieldBuilder = FieldSchemaArtifact.listFieldBuilder();
                if (constraints.hasPermissibleValues()) {
                    for (String value: constraints.getPermissibleValues()) {
                        listFieldBuilder = listFieldBuilder.withOption(value);
                    }
                }
                return listFieldBuilder;
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
        checkNodeHasNonNull(headNode, JsonKeys.VALUEDOMAIN, JsonKeys.VALUEDOMAIN);
        JsonNode currentNode = headNode.get(JsonKeys.VALUEDOMAIN);
        checkNodeHasNonNull(currentNode, JsonKeys.DATATYPE,
                String.format("%s/%s", JsonKeys.VALUEDOMAIN, JsonKeys.DATATYPE));
        return nodeToCleanedString(currentNode.get(JsonKeys.DATATYPE));
    }

    /*
    Returns the value at {"tinyId": "value"}
     */
    protected String getTinyId(JsonNode headNode) throws InvalidJsonPathException {
        checkNodeHasNonNull(headNode, JsonKeys.TINYID, JsonKeys.TINYID);
        return nodeToCleanedString(headNode.get(JsonKeys.TINYID));
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
        checkNodeHasNonNull(headNode, JsonKeys.VALUEDOMAIN, JsonKeys.VALUEDOMAIN);
        JsonNode currentNode = headNode.get(JsonKeys.VALUEDOMAIN);
        checkNodeHasNonNull(currentNode, JsonKeys.PERMISSIBLEVALUES,
                String.format("%s/%s/", JsonKeys.VALUEDOMAIN, JsonKeys.PERMISSIBLEVALUES));
        currentNode = currentNode.get(JsonKeys.PERMISSIBLEVALUES);

        HashSet<String> permissibleValuesSet = new HashSet<>();
        ArrayList<String> permissibleValues = new ArrayList<>();
        if (!currentNode.isEmpty()) {
            for (int i = 0; i < currentNode.size(); i++) {
                JsonNode node = currentNode.get(i);
                if (node.hasNonNull(JsonKeys.VALUEMEANINGNAME)) {
                    String permissibleValue = String.format("%s - %s",
                            nodeToCleanedString(node.get(JsonKeys.PERMISSIBLEVALUE)),
                            nodeToCleanedString(node.get(JsonKeys.VALUEMEANINGNAME)));
                    if (!permissibleValuesSet.contains(permissibleValue)) {
                        permissibleValuesSet.add(permissibleValue);
                        permissibleValues.add(permissibleValue);
                    }
                } else {
                    checkNodeHasNonNull(node, JsonKeys.PERMISSIBLEVALUE,
                            String.format("%s/%s/%d/%s", JsonKeys.VALUEDOMAIN, JsonKeys.PERMISSIBLEVALUES,
                                    i, JsonKeys.PERMISSIBLEVALUE));
                    String permissibleValue = nodeToCleanedString(node.get(JsonKeys.PERMISSIBLEVALUE));
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
        checkNodeHasNonNull(headNode, JsonKeys.DEFINITIONS, JsonKeys.DEFINITIONS);
        JsonNode currentNode = headNode.get(JsonKeys.DEFINITIONS);
        if (currentNode.isEmpty()) {
            return Optional.empty();
        } else {
            // check only first index in the array
            checkNodeHasNonNull(currentNode, 0, String.format("%s/%d", JsonKeys.DEFINITION, 0));
            currentNode = currentNode.get(0);
            checkNodeHasNonNull(currentNode, JsonKeys.DEFINITION,
                    String.format("%s/%d/%s", JsonKeys.DEFINITIONS, 0, JsonKeys.DEFINITION));
            return Optional.of(nodeToCleanedString(currentNode.get(JsonKeys.DEFINITION)));
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
        checkNodeHasNonNull(headNode, JsonKeys.DESIGNATIONS, JsonKeys.DESIGNATIONS);
        JsonNode designationsNode = headNode.get(JsonKeys.DESIGNATIONS);
        Optional<String> preferredLabel = Optional.empty();
        HashSet<String> alternateLabelsSet = new HashSet<>();
        ArrayList<String> alternateLabels = new ArrayList<>();
        if (designationsNode.isEmpty()) {
            throw new DesignationNotFoundException();
        } else {
            for (int i = 0; i < designationsNode.size(); i++) {
                JsonNode node = designationsNode.get(i);
                checkNodeHasNonNull(node, JsonKeys.TAGS,
                        String.format("%s/%d/%s", JsonKeys.DESIGNATIONS, i, JsonKeys.TAGS));
                JsonNode tagsNode = node.get(JsonKeys.TAGS);

                checkNodeHasNonNull(node, JsonKeys.DESIGNATION,
                        String.format("%s/%d/%s", JsonKeys.DESIGNATIONS, i, JsonKeys.DESIGNATION));
                JsonNode designationNode = node.get(JsonKeys.DESIGNATION);
                if (tagsNode.isEmpty()) {
                    String alternateLabel = nodeToCleanedString(designationNode);
                    if (!alternateLabelsSet.contains(alternateLabel)) {
                        alternateLabels.add(alternateLabel);
                        alternateLabelsSet.add(alternateLabel);
                    }
                } else {
                    List<String> tags = stringListReader.readValue(tagsNode);
                    if (tags.contains(JsonKeys.PREFERREDQUESTIONTEXT)) {
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
    private CDEConstraints getTextConstraints(JsonNode headNode) {
        CDEConstraints.CDEConstraintsBuilder builder = new CDEConstraints.CDEConstraintsBuilder();
        JsonNode currentNode = headNode.get(JsonKeys.VALUEDOMAIN);
        if (!currentNode.has(JsonKeys.DATATYPETEXT)) {
            return builder.build();
        }
        currentNode = currentNode.get(JsonKeys.DATATYPETEXT);

        if (currentNode.has(JsonKeys.MINLENGTH)) {
            builder = builder.withMinLength(currentNode.get(JsonKeys.MINLENGTH).intValue());
        }
        if (currentNode.has(JsonKeys.MAXLENGTH)) {
            builder = builder.withMaxLength(currentNode.get(JsonKeys.MAXLENGTH).intValue());
        }
        return builder.build();
    }

    /*
    Parse minValue, maxValue, and precision if they are present in the CDE.
     */
    private CDEConstraints getNumberConstraints(JsonNode headNode) {
        CDEConstraints.CDEConstraintsBuilder builder = new CDEConstraints.CDEConstraintsBuilder();
        JsonNode currentNode = headNode.get(JsonKeys.VALUEDOMAIN);
        if (!currentNode.has(JsonKeys.DATATYPENUMBER)) {
            return builder.build();
        }
        currentNode = currentNode.get(JsonKeys.DATATYPENUMBER);

        // if precision is specified and it is not zero, the min/max values must be double
        if (currentNode.has(JsonKeys.PRECISION)) {
            Integer precision = currentNode.get(JsonKeys.PRECISION).intValue();
            builder = builder.withNumericPrecision(precision);
            if (precision == 0) {
                if (currentNode.has(JsonKeys.MINVALUE)) {
                    builder = builder.withMinValue(currentNode.get(JsonKeys.MINVALUE).intValue());
                }
                if (currentNode.has(JsonKeys.MAXVALUE)) {
                    builder = builder.withMinValue(currentNode.get(JsonKeys.MAXVALUE).intValue());
                }
            } else {
                if (currentNode.has(JsonKeys.MINVALUE)) {
                    builder = builder.withMinValue(currentNode.get(JsonKeys.MINVALUE).doubleValue());
                }
                if (currentNode.has(JsonKeys.MAXVALUE)) {
                    builder = builder.withMinValue(currentNode.get(JsonKeys.MAXVALUE).doubleValue());
                }
            }
        } else {
            if (currentNode.has(JsonKeys.MINVALUE)) {
                builder = builder.withMinValue(currentNode.get(JsonKeys.MINVALUE).intValue());
            }
            if (currentNode.has(JsonKeys.MAXVALUE)) {
                builder = builder.withMinValue(currentNode.get(JsonKeys.MAXVALUE).intValue());
            }
        }
        return builder.build();
    }

    /*
    Parse the precision of the date specification if present in the CDE.
     */
    private CDEConstraints getDateConstraints(JsonNode headNode) {
        CDEConstraints.CDEConstraintsBuilder builder = new CDEConstraints.CDEConstraintsBuilder();
        JsonNode currentNode = headNode.get(JsonKeys.VALUEDOMAIN);
        if (!currentNode.has(JsonKeys.DATATYPEDATE)) {
            return builder.build();
        }
        currentNode = currentNode.get(JsonKeys.DATATYPEDATE);

        if (currentNode.has(JsonKeys.PRECISION)) {
            builder = builder.withDatePrecision(nodeToCleanedString(currentNode.get(JsonKeys.PRECISION)));
        }
        return builder.build();
    }

    /*
    Parse the permissible value options specified by a CDE.
     */
    private CDEConstraints getValueListConstraints(JsonNode headNode) throws InvalidJsonPathException {
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
    private CDEConstraints getConstraints(JsonNode headNode, String datatype) throws InvalidJsonPathException {
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

    /*
    Read CDEs and populate important fields that can be converted into a CEDAR artifact.
     */
    private ArrayList<FieldSchemaArtifact> parseCDEFile(String fileName)
            throws IOException, InvalidJsonPathException, DesignationNotFoundException, UnsupportedDataTypeException {
        JsonNode jsonData = readJsonToTree(fileName);
        ArrayList<FieldSchemaArtifact> cedarArtifacts = new ArrayList<>();
        for (JsonNode cdeNode: jsonData) {
            String datatype = getDatatype(cdeNode);
            String tinyId = getTinyId(cdeNode);
            CDEConstraints constraints = getConstraints(cdeNode, datatype);
            Optional<String> definition = getDefinition(cdeNode);
            CDEDesignations designations = getDesignations(cdeNode);
            String name = getName(designations);
            String preferredLabel = designations.getPreferredLabel();
            ArrayList<String> alternateLabels = designations.getAlternateLabels();

            FieldSchemaArtifact fieldSchemaArtifact = makeCedarArtifact(
                    datatype, constraints, tinyId, definition, preferredLabel,
                    alternateLabels,name);
            cedarArtifacts.add(fieldSchemaArtifact);
        }
        return cedarArtifacts;
    }

    public ArrayList<ObjectNode> convertCDEsToCEDAR(String filename)
            throws IOException, InvalidJsonPathException, DesignationNotFoundException, UnsupportedDataTypeException {
        ArrayList<FieldSchemaArtifact> cedarArtifacts = parseCDEFile(filename);
        ArrayList<ObjectNode> jsonRenders = new ArrayList<>();
        for (FieldSchemaArtifact artifact: cedarArtifacts) {
            ObjectNode rendering = jsonSchemaArtifactRenderer.renderFieldSchemaArtifact(artifact);
            jsonRenders.add(rendering);
        }
        return jsonRenders;
    }
}
