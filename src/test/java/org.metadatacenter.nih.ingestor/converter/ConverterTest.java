package org.metadatacenter.nih.ingestor.converter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.metadatacenter.nih.ingestor.exceptions.DesignationNotFoundException;
import org.metadatacenter.nih.ingestor.exceptions.InvalidJsonPathException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ConverterTest {
    ObjectMapper mapper = new ObjectMapper();
    Converter converter = new Converter();

    @Test
    public void testNodeToCleanedString() throws IOException {
        String testKey = "hello";
        String testValue = "world";
        JsonNode node = mapper.readTree("{\"hello\": \"world\"}");
        assertEquals(testValue, converter.nodeToCleanedString(node.get(testKey)));
    }

    @Test(expected = InvalidJsonPathException.class)
    public void testNodeHasNonNull_withNull() throws IOException, InvalidJsonPathException {
        String testKey = "key";
        JsonNode node = mapper.readTree("{\"differentKey\": null}");
        converter.checkNodeHasNonNull(node, testKey, testKey);
    }

    @Test
    public void testNodeHasNonNull_withNonNull() throws IOException, InvalidJsonPathException {
        String testKey = "key";
        JsonNode node = mapper.readTree("{\"key\": \"value\"}");
        converter.checkNodeHasNonNull(node, testKey, testKey);
    }

    @Test
    public void testNodeHasNonNull_withArrayIndex() throws IOException, InvalidJsonPathException {
        String testKey = "key";
        int testIndex = 1;
        JsonNode node = mapper.readTree("{\"key\": [\"val1\", \"val2\"]}");
        converter.checkNodeHasNonNull(node.get(testKey), testIndex, String.format("%d", testIndex));
    }

    @Test(expected = InvalidJsonPathException.class)
    public void testNodeHasNonNull_withOutOfBoundsArrayIndex() throws IOException, InvalidJsonPathException {
        String testKey = "key";
        int testIndex = 2;
        JsonNode node = mapper.readTree("{\"key\": [\"val1\", \"val2\"]}");
        converter.checkNodeHasNonNull(node.get(testKey), testIndex, String.format("%d", testIndex));
    }

    @Test
    public void testGetDatatype() throws IOException, InvalidJsonPathException {
        String refDatatype = "aaaaa";
        String testJson = """
                {"valueDomain": {"datatype": "aaaaa"}}
                """;
        JsonNode node = mapper.readTree(testJson);
        assertEquals(refDatatype, converter.getDatatype(node));
    }

    @Test
    public void testGetTinyId() throws IOException, InvalidJsonPathException {
        String refId = "testId";
        String testJson = """
                {"tinyId": "testId"}
                """;
        JsonNode node = mapper.readTree(testJson);
        assertEquals(refId, converter.getTinyId(node));
    }

    @Test
    public void testGetPermissibleValues_withValueMeaningNames() throws IOException, InvalidJsonPathException {
        ArrayList<String> refPermissibleValues = new ArrayList<>();
        refPermissibleValues.add("1 - value_1");
        refPermissibleValues.add("2 - value_1");
        String testJson = """
                {
                    "valueDomain": {
                    "datatype": "Value List",
                        "permissibleValues": [
                            {
                                "permissibleValue": "1",
                                "valueMeaningName": "value_1"
                            },
                            {
                                "permissibleValue": "2",
                                "valueMeaningName": "value_1"
                            }
                        ]
                    }
                }
                """;
        JsonNode node = mapper.readTree(testJson);
        assertEquals(refPermissibleValues, converter.getPermissibleValues(node));
    }

    @Test
    public void testGetPermissibleValues_withDuplicateValueMeaningNames() throws IOException, InvalidJsonPathException {
        ArrayList<String> refPermissibleValues = new ArrayList<>();
        refPermissibleValues.add("1 - value_1");
        String testJson = """
                {
                    "valueDomain": {
                    "datatype": "Value List",
                        "permissibleValues": [
                            {
                                "permissibleValue": "1",
                                "valueMeaningName": "value_1"
                            },
                            {
                                "permissibleValue": "1",
                                "valueMeaningName": "value_1"
                            }
                        ]
                    }
                }
                """;
        JsonNode node = mapper.readTree(testJson);
        assertEquals(refPermissibleValues, converter.getPermissibleValues(node));
    }

    @Test
    public void testGetPermissibleValues_withOnlyPermissibleValue() throws IOException, InvalidJsonPathException {
        ArrayList<String> refPermissibleValues = new ArrayList<>();
        refPermissibleValues.add("value_1");
        refPermissibleValues.add("value_2");
        String testJson = """
                {
                    "valueDomain": {
                    "datatype": "Value List",
                        "permissibleValues": [
                            {
                                "permissibleValue": "value_1"
                            },
                            {
                                "permissibleValue": "value_2"
                            }
                        ]
                    }
                }
                """;
        JsonNode node = mapper.readTree(testJson);
        assertEquals(refPermissibleValues, converter.getPermissibleValues(node));
    }

    @Test
    public void testGetPermissibleValues_withDuplicatePermissibleValues() throws IOException, InvalidJsonPathException {
        ArrayList<String> refPermissibleValues = new ArrayList<>();
        refPermissibleValues.add("value_1");
        String testJson = """
                {
                    "valueDomain": {
                    "datatype": "Value List",
                        "permissibleValues": [
                            {
                                "permissibleValue": "value_1"
                            },
                            {
                                "permissibleValue": "value_1"
                            }
                        ]
                    }
                }
                """;
        JsonNode node = mapper.readTree(testJson);
        assertEquals(refPermissibleValues, converter.getPermissibleValues(node));
    }

    @Test
    public void testGetDefinition_withDefinition() throws IOException, InvalidJsonPathException {
        Optional<String> refDefinition = Optional.of("value");
        String testJson = """
                {
                    "definitions": [
                        {
                            "definition": "value",
                            "tags": []
                        }
                    ]
                }
                """;
        JsonNode node = mapper.readTree(testJson);
        Optional<String> definition = converter.getDefinition(node);
        assertTrue(definition.isPresent());
        assertEquals(refDefinition, definition);
    }

    @Test
    public void testGetDefinition_empty() throws IOException, InvalidJsonPathException {
        Optional<String> refDefinition = Optional.empty();
        String testJson = """
                {
                    "definitions": []
                }
                """;
        JsonNode node = mapper.readTree(testJson);
        Optional<String> definition = converter.getDefinition(node);
        assertTrue(definition.isEmpty());
        assertEquals(refDefinition, definition);
    }

    @Test
    public void testGetDesignations_withPreferredQuestionText() throws IOException, InvalidJsonPathException, DesignationNotFoundException {
        ArrayList<String> refAltLabels = new ArrayList<>();
        refAltLabels.add("value_1");
        refAltLabels.add("value_2");
        String refPrefLabel = "value_3";
        String testJson = """
                {
                    "designations": [
                        {
                            "designation": "value_1",
                            "tags": []
                        },
                        {
                            "designation": "value_2",
                            "tags": ["Some Other Text"]
                        },
                        {
                            "designation": "value_3",
                            "tags": ["\\"Preferred Question Text\\""]
                        }
                    ]
                }
                """;
        JsonNode node = mapper.readTree(testJson);
        CDEDesignations designations = converter.getDesignations(node);
        assertEquals(refAltLabels, designations.getAlternateLabels());
        assertEquals(refPrefLabel, designations.getPreferredLabel());
    }

    @Test
    public void testGetDesignations_withDuplicates() throws IOException, InvalidJsonPathException, DesignationNotFoundException {
        ArrayList<String> refAltLabels = new ArrayList<>();
        refAltLabels.add("value_1");
        refAltLabels.add("value_2");
        String refPrefLabel = "value_1";
        String testJson = """
                {
                    "designations": [
                        {
                            "designation": "value_1",
                            "tags": []
                        },
                        {
                            "designation": "value_2",
                            "tags": ["Some Other Text"]
                        },
                        {
                            "designation": "value_2",
                            "tags": []
                        }
                    ]
                }
                """;
        JsonNode node = mapper.readTree(testJson);
        CDEDesignations designations = converter.getDesignations(node);
        assertEquals(refAltLabels, designations.getAlternateLabels());
        assertEquals(refPrefLabel, designations.getPreferredLabel());
    }

    @Test
    public void testGetDesignations_withNoPreferredQuestionText() throws IOException, InvalidJsonPathException, DesignationNotFoundException {
        ArrayList<String> refAltLabels = new ArrayList<>();
        refAltLabels.add("value_1");
        refAltLabels.add("value_2");
        refAltLabels.add("value_3");
        String refPrefLabel = "value_1";
        String testJson = """
                {
                    "designations": [
                        {
                            "designation": "value_1",
                            "tags": []
                        },
                        {
                            "designation": "value_2",
                            "tags": ["Some Other Text"]
                        },
                        {
                            "designation": "value_3",
                            "tags": []
                        }
                    ]
                }
                """;
        JsonNode node = mapper.readTree(testJson);
        CDEDesignations designations = converter.getDesignations(node);
        assertEquals(refAltLabels, designations.getAlternateLabels());
        assertEquals(refPrefLabel, designations.getPreferredLabel());
    }

    @Test(expected = DesignationNotFoundException.class)
    public void testGetDesignations_empty() throws IOException, InvalidJsonPathException, DesignationNotFoundException {
        String testJson = """
                {
                    "designations": []
                }
                """;
        JsonNode node = mapper.readTree(testJson);
        CDEDesignations designations = converter.getDesignations(node);
    }

    @Test
    public void testGetName() {
        String name = "name";
        ArrayList<String> altLabels = new ArrayList<>();
        altLabels.add(name);
        altLabels.add("altLabel_2");
        altLabels.add("altLabel_3");
        CDEDesignations testDesignations = new CDEDesignations("prefLabel", altLabels);
        assertEquals(name, converter.getName(testDesignations));
    }

    @Test
    public void testGetName_noAltLabels() {
        String name = "prefLabel";
        ArrayList<String> altLabels = new ArrayList<>();
        CDEDesignations testDesignations = new CDEDesignations(name, altLabels);
        assertEquals(name, converter.getName(testDesignations));
    }

    // testChooseBuilderByType

}
