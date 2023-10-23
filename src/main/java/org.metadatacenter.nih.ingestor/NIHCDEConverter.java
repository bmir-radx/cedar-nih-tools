package org.metadatacenter.nih.ingestor;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.metadatacenter.artifacts.model.core.*;
import org.metadatacenter.artifacts.model.core.builders.FieldSchemaArtifactBuilder;
import org.metadatacenter.artifacts.model.core.builders.ListFieldBuilder;
import org.metadatacenter.artifacts.model.core.builders.NumericFieldBuilder;
import org.metadatacenter.artifacts.model.core.builders.TextFieldBuilder;
import org.metadatacenter.artifacts.model.renderer.JsonSchemaArtifactRenderer;
import org.metadatacenter.nih.ingestor.constants.DataTypes;
import org.metadatacenter.nih.ingestor.constants.JsonKeys;
import org.metadatacenter.nih.ingestor.exceptions.UnsupportedDataTypeException;

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.util.Optional;

public class NIHCDEConverter {

    /*
    Read a json file into a JsonNode from its filepath.
    This is Jackson's tree representation of the Json document.
     */
    private static JsonNode readJsonToTree(String fileName) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = new String(Files.readAllBytes(Paths.get(fileName)));
        return mapper.readTree(jsonString);
    }

    private static FieldSchemaArtifactBuilder chooseBuilderByDataType(String inputType, List<String> permissibleValues)
            throws UnsupportedDataTypeException {
        switch (inputType) {
            case DataTypes.FILE:
            case DataTypes.EXTERNALLYDEFINED:
            case DataTypes.TEXT:
                return FieldSchemaArtifact.textFieldBuilder().
                        withMinLength(2).
                        withMaxLength(10).
                        // bug where false will not serialize but true does
                        withRequiredValue(false);
            case DataTypes.NUMBER:
                return FieldSchemaArtifact.numericFieldBuilder().
                        withNumericType(NumericType.INTEGER).
                        withRequiredValue(false);
            case DataTypes.DATE:
                return FieldSchemaArtifact.temporalFieldBuilder().
                        withTemporalType(TemporalType.DATE).
                        withTemporalGranularity(TemporalGranularity.DAY);
            case DataTypes.TIME:
                return FieldSchemaArtifact.temporalFieldBuilder().
                        withTemporalType(TemporalType.TIME).
                        // assumed minute. nothing in the CDE indicates time granularity.
                        withTemporalGranularity(TemporalGranularity.MINUTE);
            case DataTypes.VALUELIST:
                ListFieldBuilder builder = FieldSchemaArtifact.listFieldBuilder();
                for (String option: permissibleValues) {
                    builder = builder.withOption(option);
                }
                return builder;
            default:
                // will not be reached if valid data type is used
                throw new UnsupportedDataTypeException(inputType);
        }
    }

    private static FieldSchemaArtifact convertToCedarArtifact(String inputType,
                                                              List<String> permissibleValues,
                                                              String tinyId,
                                                              Optional<String> definition,
                                                              String preferredQuestionText,
                                                              List<String> designations,
                                                              String name)
            throws UnsupportedDataTypeException {

        FieldSchemaArtifactBuilder builder = chooseBuilderByDataType(inputType, permissibleValues);
        if (definition.isPresent()) {
            builder = builder.withDescription(definition.get());
        }
        return builder.withName(name).
                withIdentifier(tinyId).
                withPreferredLabel(preferredQuestionText).
                withAlternateLabels(designations).
                build();
    }

    private static String cleanJsonString(String verbatimString) {
        // delete the leading and ending double quote characters
        // also ensure that those are also the leading and ending characters
        if (verbatimString.startsWith("\"") && verbatimString.endsWith("\"")) {
            return verbatimString.substring(1, verbatimString.length()-1);
        } else {
            return verbatimString;
        }
    }

    public static void main( String[] args ) throws IOException {

        String fileName = args[0];

        // URI createdBy = new URI("https://metadatacenter.org/users/e5e2c5fd-03d3-4e5d-9dba-2897e568eaf8");

        // could parse everything into a dataclass instead of handling the JsonTree directly
        JsonNode jsonData = readJsonToTree(fileName);
        ArrayList<ObjectNode> jsonArtifacts = new ArrayList<>();
        JsonSchemaArtifactRenderer jsonSchemaArtifactRenderer = new JsonSchemaArtifactRenderer();
        for (JsonNode item: jsonData) {
            String inputType = cleanJsonString(item.get(JsonKeys.VALUEDOMAIN).get(JsonKeys.DATATYPE).toString());
            String tinyId = cleanJsonString(item.get(JsonKeys.TINYID).toString());

            // String createdBy = cleanJsonString(item.get(JsonKeys.CREATEDBY).get(JsonKeys.USERNAME).toString());

            // the definition field is not defined in some CDEs
            // leave this blank by default
            Optional<String> definition;
            JsonNode definitionsNode = item.get(JsonKeys.DEFINITIONS);
            if (!definitionsNode.isEmpty()) {
                definition = Optional.of(cleanJsonString(definitionsNode.get(0).get(JsonKeys.DEFINITION).toString()));
            } else {
                definition = Optional.empty();
            }

            // permissibleValues is always a populated field in the JSON, but it can be an empty array
            JsonNode permissibleValuesNode = item.get(JsonKeys.VALUEDOMAIN).get(JsonKeys.PERMISSIBLEVALUES);
            ArrayList<String> permissibleValues = new ArrayList<>();
            for (JsonNode node: permissibleValuesNode) {
                permissibleValues.add(cleanJsonString(node.toString()));
            }

            String preferredQuestionText = "";
            ArrayList<String> designations = new ArrayList<>();
            JsonNode designationsNode = item.get(JsonKeys.DESIGNATIONS);
            for (JsonNode node : designationsNode) {
                JsonNode tags = node.get(JsonKeys.TAGS);
                if (!tags.isEmpty()) {
                    if (tags.get(0).toString().equals(JsonKeys.PREFERREDQUESTIONTEXT)) {
                        preferredQuestionText = cleanJsonString(node.get(JsonKeys.DESIGNATION).toString());
                    } else {
                        designations.add(cleanJsonString(node.get(JsonKeys.DESIGNATION).toString()));
                    }
                }
            }
            if (preferredQuestionText.isEmpty() && designations.size() > 0) {
                // might want this to throw if we have no preferred question text OR designations
                preferredQuestionText = designations.get(0);
            }

            // primary name field gets the first designation
            // if the first designation is not present, it gets the preferred question text
            String name = designations.isEmpty() ? preferredQuestionText : designations.get(0);

            try {
                FieldSchemaArtifact fieldSchemaArtifact = convertToCedarArtifact(
                        inputType, permissibleValues, tinyId, definition, preferredQuestionText,
                        designations, name
                );
                ObjectNode rendering = jsonSchemaArtifactRenderer.renderFieldSchemaArtifact(fieldSchemaArtifact);
                jsonArtifacts.add(rendering);
            } catch (UnsupportedDataTypeException e) {
                System.out.println(e);
            }
        }
        for (ObjectNode item: jsonArtifacts) {
            System.out.println(item);
        }
    }
}