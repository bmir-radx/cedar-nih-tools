package org.metadatacenter.nih.ingestor;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.metadatacenter.artifacts.model.core.FieldSchemaArtifact;
import org.metadatacenter.artifacts.model.core.NumericType;
import org.metadatacenter.artifacts.model.core.TemporalGranularity;
import org.metadatacenter.artifacts.model.core.builders.ListFieldBuilder;
import org.metadatacenter.artifacts.model.core.TemporalType;
import org.metadatacenter.nih.ingestor.constants.DataTypes;
import org.metadatacenter.nih.ingestor.constants.JsonKeys;
import org.metadatacenter.nih.ingestor.exceptions.UnsupportedDataTypeException;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;

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

    private static FieldSchemaArtifact convertToCedarArtifact(String inputType,
                                                              List<String> permissibleValues,
                                                              String tinyId,
                                                              String definition,
                                                              String preferredQuestionText,
                                                              List<String> designations,
                                                              String name) throws UnsupportedDataTypeException {
        FieldSchemaArtifact fieldSchemaArtifact;
        switch (inputType) {
            case DataTypes.TEXT:
                fieldSchemaArtifact = FieldSchemaArtifact.textFieldBuilder().
                        withName(name).
                        withJsonSchemaTitle(name).
                        withDescription(definition).
                        withJsonSchemaDescription(definition).
                        withIdentifier(tinyId).
                        withPreferredLabel(preferredQuestionText).
                        withAlternateLabels(designations).
                        build();
                break;
            case DataTypes.NUMBER:
                fieldSchemaArtifact = FieldSchemaArtifact.numericFieldBuilder().
                        // numeric types are not specified in the CDEs
                        // chose integer after inspecting a few CDEs manually
                        withNumericType(NumericType.INTEGER).
                        withName(name).
                        withJsonSchemaTitle(name).
                        withDescription(definition).
                        withJsonSchemaDescription(definition).
                        withIdentifier(tinyId).
                        withPreferredLabel(preferredQuestionText).
                        withAlternateLabels(designations).
                        build();
                break;
            case DataTypes.DATE:
                fieldSchemaArtifact = FieldSchemaArtifact.temporalFieldBuilder().
                        withTemporalType(TemporalType.DATE).
                        withTemporalGranularity(TemporalGranularity.DAY).
                        withName(name).
                        withJsonSchemaTitle(name).
                        withDescription(definition).
                        withJsonSchemaDescription(definition).
                        withIdentifier(tinyId).
                        withPreferredLabel(preferredQuestionText).
                        withAlternateLabels(designations).
                        build();
                break;
            case DataTypes.TIME:
                fieldSchemaArtifact = FieldSchemaArtifact.temporalFieldBuilder().
                        withTemporalType(TemporalType.TIME).
                        // assumed minute. nothing in the CDE indicates time granularity.
                        withTemporalGranularity(TemporalGranularity.MINUTE).
                        withName(name).
                        withJsonSchemaTitle(name).
                        withDescription(definition).
                        withJsonSchemaDescription(definition).
                        withIdentifier(tinyId).
                        withPreferredLabel(preferredQuestionText).
                        withAlternateLabels(designations).
                        build();
                break;
            case DataTypes.VALUELIST:
                ListFieldBuilder builder = FieldSchemaArtifact.listFieldBuilder().
                        withName(name).
                        withJsonSchemaTitle(name).
                        withDescription(definition).
                        withJsonSchemaDescription(definition).
                        withIdentifier(tinyId).
                        withPreferredLabel(preferredQuestionText).
                        withAlternateLabels(designations);
                for (String option: permissibleValues) {
                    builder = builder.withOption(option);
                }
                fieldSchemaArtifact = builder.build();
                break;
            case DataTypes.FILE:
            case DataTypes.EXTERNALLYDEFINED:
            default:
                // should be its own exception for an unknown data type
                // will not be reached if valid data type is used
                throw new UnsupportedDataTypeException(inputType);
        }
        return fieldSchemaArtifact;
    }

    private static String cleanJsonString(String verbatimString) {
        // delete the leading and ending double quote characters
        return verbatimString.substring(1, verbatimString.length()-1);
    }

    public static void main( String[] args ) throws IOException {

        JsonNode jsonData = readJsonToTree("/Users/jkyu/Projects/CDE_Ingestion/SearchExport.json");
        for (JsonNode item: jsonData) {
            String inputType = cleanJsonString(item.get(JsonKeys.VALUEDOMAIN).get(JsonKeys.DATATYPE).toString());
            String tinyId = cleanJsonString(item.get(JsonKeys.TINYID).toString());

            // the definition field is not defined in some CDEs
            // leave this blank by default
            String definition;
            JsonNode definitionsNode = item.get(JsonKeys.DEFINITIONS);
            if (!definitionsNode.isEmpty()) {
                definition = cleanJsonString(definitionsNode.get(0).get(JsonKeys.DEFINITION).toString());
            } else {
                definition = "";
            }

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
                System.out.println(fieldSchemaArtifact);
            } catch (UnsupportedDataTypeException e) {
                System.out.println(e);
            }
        }
    }
}