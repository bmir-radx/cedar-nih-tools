package org.metadatacenter.nih.ingestor.converter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Test;
import org.metadatacenter.artifacts.model.core.NumericType;
import org.metadatacenter.nih.ingestor.constants.DataTypes;
import org.metadatacenter.nih.ingestor.constants.JsonDatePrecisions;
import org.metadatacenter.nih.ingestor.exceptions.DesignationNotFoundException;
import org.metadatacenter.nih.ingestor.exceptions.InvalidDatePrecisionException;
import org.metadatacenter.nih.ingestor.exceptions.InvalidJsonPathException;
import org.metadatacenter.nih.ingestor.exceptions.UnsupportedDataTypeException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
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
    public void testGetValueListConstraints() throws IOException, InvalidJsonPathException {
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
        CDEConstraints constraints = converter.getValueListConstraints(node);
        assertTrue(constraints.hasPermissibleValues());
        assertEquals(refPermissibleValues, constraints.getPermissibleValues());
    }

    @Test
    public void testGetValueListConstraints_empty() throws IOException, InvalidJsonPathException {
        String testJson = """
                {
                    "valueDomain": {
                        "datatype": "Value List",
                        "permissibleValues": []
                    }
                }
                """;
        JsonNode node = mapper.readTree(testJson);
        CDEConstraints constraints = converter.getValueListConstraints(node);
        assertFalse(constraints.hasPermissibleValues());
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
        converter.getDesignations(node);
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

    @Test
    public void testGetTextConstraints() throws IOException {
        Integer refMinLength = 1;
        Integer refMaxLength = 255;
        String testJson = """
                {
                    "valueDomain": {
                        "datatype": "Text",
                        "datatypeText": {
                            "minLength": 1,
                            "maxLength": 255
                        }
                    }
                }
                """;
        JsonNode node = mapper.readTree(testJson);
        CDEConstraints constraints = converter.getTextConstraints(node);
        assertTrue(constraints.hasMinLength());
        assertEquals(refMinLength, constraints.getMinLength());
        assertTrue(constraints.hasMaxLength());
        assertEquals(refMaxLength, constraints.getMaxLength());
    }

    @Test
    public void testGetTextConstraints_onlyMinLength() throws IOException {
        Integer refMinLength = 1;
        String testJson = """
                {
                    "valueDomain": {
                        "datatype": "Text",
                        "datatypeText": {
                            "minLength": 1
                        }
                    }
                }
                """;
        JsonNode node = mapper.readTree(testJson);
        CDEConstraints constraints = converter.getTextConstraints(node);
        assertTrue(constraints.hasMinLength());
        assertEquals(refMinLength, constraints.getMinLength());
        assertFalse(constraints.hasMaxLength());
    }

    @Test
    public void testGetTextConstraints_onlyMaxLength() throws IOException {
        Integer refMaxLength = 255;
        String testJson = """
                {
                    "valueDomain": {
                        "datatype": "Text",
                        "datatypeText": {
                            "maxLength": 255
                        }
                    }
                }
                """;
        JsonNode node = mapper.readTree(testJson);
        CDEConstraints constraints = converter.getTextConstraints(node);
        assertFalse(constraints.hasMinLength());
        assertTrue(constraints.hasMaxLength());
        assertEquals(refMaxLength, constraints.getMaxLength());
    }

    @Test
    public void testGetTextConstraints_noConstraints() throws IOException {
        String testJson = """
                {
                    "valueDomain": {
                        "datatype": "Text"
                    }
                }
                """;
        JsonNode node = mapper.readTree(testJson);
        CDEConstraints constraints = converter.getTextConstraints(node);
        assertFalse(constraints.hasMinLength());
        assertFalse(constraints.hasMaxLength());
    }

    @Test
    public void testGetNumberConstraints() throws IOException {
        Integer refMinValue = 16;
        Integer refMaxValue = 73;
        Integer refPrecision = 0;
        NumericType refNumericType = NumericType.INTEGER;
        String testJson = """
                {
                    "valueDomain": {
                        "datatype": "Number",
                        "datatypeNumber": {
                            "minValue": 16,
                            "maxValue": 73,
                            "precision": 0
                        }
                    }
                }
                """;
        JsonNode node = mapper.readTree(testJson);
        CDEConstraints constraints = converter.getNumberConstraints(node);
        assertTrue(constraints.hasMinValue());
        assertEquals(refMinValue, constraints.getMinValue());
        assertTrue(constraints.hasMaxValue());
        assertEquals(refMaxValue, constraints.getMaxValue());
        assertTrue(constraints.hasNumericPrecision());
        assertEquals(refPrecision, refPrecision);
        assertTrue(constraints.hasNumericType());
        assertEquals(refNumericType, constraints.getNumericType());
    }

    @Test
    public void testGetNumberConstraints_noPrecision_integer() throws IOException {
        Number refMinValue = 16;
        Number refMaxValue = 73;
        NumericType refNumericType = NumericType.INTEGER;
        String testJson = """
                {
                    "valueDomain": {
                        "datatype": "Number",
                        "datatypeNumber": {
                            "minValue": 16,
                            "maxValue": 73
                        }
                    }
                }
                """;
        JsonNode node = mapper.readTree(testJson);
        CDEConstraints constraints = converter.getNumberConstraints(node);
        assertTrue(constraints.hasMinValue());
        assertEquals(refMinValue, constraints.getMinValue());
        assertTrue(constraints.hasMaxValue());
        assertEquals(refMaxValue, constraints.getMaxValue());
        assertFalse(constraints.hasNumericPrecision());
        assertTrue(constraints.hasNumericType());
        assertEquals(refNumericType, constraints.getNumericType());
    }

    @Test
    public void testGetNumberConstraints_noPrecision_decimal() throws IOException {
        Number refMinValue = 16;
        Number refMaxValue = 73.3;
        NumericType refNumericType = NumericType.DECIMAL;
        String testJson = """
                {
                    "valueDomain": {
                        "datatype": "Number",
                        "datatypeNumber": {
                            "minValue": 16,
                            "maxValue": 73.3
                        }
                    }
                }
                """;
        JsonNode node = mapper.readTree(testJson);
        CDEConstraints constraints = converter.getNumberConstraints(node);
        assertTrue(constraints.hasMinValue());
        assertEquals(refMinValue, constraints.getMinValue());
        assertTrue(constraints.hasMaxValue());
        assertEquals(refMaxValue, constraints.getMaxValue());
        assertFalse(constraints.hasNumericPrecision());
        assertTrue(constraints.hasNumericType());
        assertEquals(refNumericType, constraints.getNumericType());
    }

    @Test
    public void testGetNumberConstraints_noMinValue() throws IOException {
        Number refMaxValue = 73.3;
        NumericType refNumericType = NumericType.DECIMAL;
        String testJson = """
                {
                    "valueDomain": {
                        "datatype": "Number",
                        "datatypeNumber": {
                            "maxValue": 73.3
                        }
                    }
                }
                """;
        JsonNode node = mapper.readTree(testJson);
        CDEConstraints constraints = converter.getNumberConstraints(node);
        assertFalse(constraints.hasMinValue());
        assertTrue(constraints.hasMaxValue());
        assertEquals(refMaxValue, constraints.getMaxValue());
        assertFalse(constraints.hasNumericPrecision());
        assertTrue(constraints.hasNumericType());
        assertEquals(refNumericType, constraints.getNumericType());
    }

    @Test
    public void testGetNumberConstraints_noMaxValue() throws IOException {
        Number refMinValue = 16;
        NumericType refNumericType = NumericType.INTEGER;
        String testJson = """
                {
                    "valueDomain": {
                        "datatype": "Number",
                        "datatypeNumber": {
                            "minValue": 16
                        }
                    }
                }
                """;
        JsonNode node = mapper.readTree(testJson);
        CDEConstraints constraints = converter.getNumberConstraints(node);
        assertTrue(constraints.hasMinValue());
        assertEquals(refMinValue, constraints.getMinValue());
        assertFalse(constraints.hasMaxValue());
        assertFalse(constraints.hasNumericPrecision());
        assertTrue(constraints.hasNumericType());
        assertEquals(refNumericType, constraints.getNumericType());
    }

    @Test
    public void testGetNumberConstraints_noConstraints() throws IOException {
        // default to decimal as numeric type if none specified
        NumericType refNumericType = NumericType.INTEGER;
        String testJson = """
                {
                    "valueDomain": {
                        "datatype": "Number"
                    }
                }
                """;
        JsonNode node = mapper.readTree(testJson);
        CDEConstraints constraints = converter.getNumberConstraints(node);
        assertFalse(constraints.hasMinValue());
        assertFalse(constraints.hasMaxValue());
        assertFalse(constraints.hasNumericPrecision());
        assertTrue(constraints.hasNumericType());
        assertEquals(refNumericType, constraints.getNumericType());
    }

    @Test
    public void testGetDateConstraints() throws IOException, InvalidDatePrecisionException {
        String refPrecision = JsonDatePrecisions.MINUTE;
        String testJson = """
                {
                    "valueDomain": {
                        "datatype": "Date",
                        "datatypeDate": {
                            "precision": "Minute"
                        }
                    }
                }
                """;
        JsonNode node = mapper.readTree(testJson);
        CDEConstraints constraints = converter.getDateConstraints(node);
        assertTrue(constraints.hasDatePrecision());
        assertEquals(refPrecision, constraints.getDatePrecision());
    }

    @Test(expected = InvalidDatePrecisionException.class)
    public void testGetDateConstraints_invalidPrecision() throws IOException, InvalidDatePrecisionException {
        String testJson = """
                {
                    "valueDomain": {
                        "datatype": "Date",
                        "datatypeDate": {
                            "precision": "Years"
                        }
                    }
                }
                """;
        JsonNode node = mapper.readTree(testJson);
        converter.getDateConstraints(node);
    }

    @Test
    public void testGetDateConstraints_noConstraints() throws IOException, InvalidDatePrecisionException {
        String testJson = """
                {
                    "valueDomain": {
                        "datatype": "Date"
                    }
                }
                """;
        JsonNode node = mapper.readTree(testJson);
        CDEConstraints constraints = converter.getDateConstraints(node);
        assertFalse(constraints.hasDatePrecision());
    }

    @Test
    public void testGetConstraints_text() throws IOException, InvalidJsonPathException, InvalidDatePrecisionException {
        Integer refMinLength = 1;
        Integer refMaxLength = 255;
        String testJson = """
                {
                    "valueDomain": {
                        "datatype": "Text",
                        "datatypeText": {
                            "minLength": 1,
                            "maxLength": 255
                        }
                    }
                }
                """;
        JsonNode node = mapper.readTree(testJson);
        CDEConstraints constraints = converter.getConstraints(node, DataTypes.TEXT);
        assertTrue(constraints.hasMinLength());
        assertEquals(refMinLength, constraints.getMinLength());
        assertTrue(constraints.hasMaxLength());
        assertEquals(refMaxLength, constraints.getMaxLength());
    }

    @Test
    public void testGetConstraints_number() throws IOException, InvalidJsonPathException, InvalidDatePrecisionException {
        Integer refMinValue = 16;
        Integer refMaxValue = 73;
        Integer refPrecision = 0;
        NumericType refNumericType = NumericType.INTEGER;
        String testJson = """
                {
                    "valueDomain": {
                        "datatype": "Number",
                        "datatypeNumber": {
                            "minValue": 16,
                            "maxValue": 73,
                            "precision": 0
                        }
                    }
                }
                """;
        JsonNode node = mapper.readTree(testJson);
        CDEConstraints constraints = converter.getConstraints(node, DataTypes.NUMBER);
        assertTrue(constraints.hasMinValue());
        assertEquals(refMinValue, constraints.getMinValue());
        assertTrue(constraints.hasMaxValue());
        assertEquals(refMaxValue, constraints.getMaxValue());
        assertTrue(constraints.hasNumericPrecision());
        assertEquals(refPrecision, refPrecision);
        assertTrue(constraints.hasNumericType());
        assertEquals(refNumericType, constraints.getNumericType());
    }

    @Test
    public void testGetConstraints_date() throws IOException, InvalidJsonPathException, InvalidDatePrecisionException {
        String refPrecision = JsonDatePrecisions.MINUTE;
        String testJson = """
                {
                    "valueDomain": {
                        "datatype": "Date",
                        "datatypeDate": {
                            "precision": "Minute"
                        }
                    }
                }
                """;
        JsonNode node = mapper.readTree(testJson);
        CDEConstraints constraints = converter.getConstraints(node, DataTypes.DATE);
        assertTrue(constraints.hasDatePrecision());
        assertEquals(refPrecision, constraints.getDatePrecision());
    }

    @Test
    public void testGetConstraints_valueList() throws IOException, InvalidJsonPathException, InvalidDatePrecisionException {
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
        CDEConstraints constraints = converter.getConstraints(node, DataTypes.VALUELIST);
        assertTrue(constraints.hasPermissibleValues());
        assertEquals(refPermissibleValues, constraints.getPermissibleValues());
    }

    @Test
    public void testGetConstraints_default() throws IOException, InvalidJsonPathException, InvalidDatePrecisionException {
        String testJson = """
                {
                    "valueDomain": {
                        "datatype": "Time"
                    }
                }
                """;
        JsonNode node = mapper.readTree(testJson);
        CDEConstraints constraints = converter.getConstraints(node, DataTypes.TIME);
        assertFalse(constraints.hasDatePrecision());
        assertFalse(constraints.hasPermissibleValues());
        assertFalse(constraints.hasMinLength());
        assertFalse(constraints.hasMaxLength());
        assertFalse(constraints.hasMinValue());
        assertFalse(constraints.hasMaxValue());
        assertFalse(constraints.hasNumericPrecision());
    }

    @Test
    public void testParseCDEFromJsonNode_text()
            throws IOException, InvalidJsonPathException, DesignationNotFoundException,
            InvalidDatePrecisionException, UnsupportedDataTypeException {
        JsonNode node = mapper.readTree(ExampleNIHCDEs.textCDE_NIH);
        ObjectNode cedarCDE = converter.convertCDEToCedar(node);
        assertEquals(ReferenceCEDARCDEs.textCDE_CEDAR, cedarCDE.toString());
    }

    @Test
    public void testParseCDEFromJsonNode_number()
            throws IOException, InvalidJsonPathException, DesignationNotFoundException,
            InvalidDatePrecisionException, UnsupportedDataTypeException {
        JsonNode node = mapper.readTree(ExampleNIHCDEs.numberCDE_NIH);
        ObjectNode cedarCDE = converter.convertCDEToCedar(node);
        assertEquals(ReferenceCEDARCDEs.numberCDE_CEDAR, cedarCDE.toString());
    }

    @Test
    public void testParseCDEFromJsonNode_date()
            throws IOException, InvalidJsonPathException, DesignationNotFoundException,
            InvalidDatePrecisionException, UnsupportedDataTypeException {
        JsonNode node = mapper.readTree(ExampleNIHCDEs.dateCDE_NIH);
        ObjectNode cedarCDE = converter.convertCDEToCedar(node);
        assertEquals(ReferenceCEDARCDEs.dateCDE_CEDAR, cedarCDE.toString());
    }

    @Test
    public void testParseCDEFromJsonNode_time()
            throws IOException, InvalidJsonPathException, DesignationNotFoundException,
            InvalidDatePrecisionException, UnsupportedDataTypeException {
        JsonNode node = mapper.readTree(ExampleNIHCDEs.timeCDE_NIH);
        ObjectNode cedarCDE = converter.convertCDEToCedar(node);
        assertEquals(ReferenceCEDARCDEs.timeCDE_CEDAR, cedarCDE.toString());
    }

    @Test
    public void testParseCDEFromJsonNode_valueList()
            throws IOException, InvalidJsonPathException, DesignationNotFoundException,
            InvalidDatePrecisionException, UnsupportedDataTypeException {
        JsonNode node = mapper.readTree(ExampleNIHCDEs.valueListCDE_NIH);
        ObjectNode cedarCDE = converter.convertCDEToCedar(node);
        assertEquals(ReferenceCEDARCDEs.valueListCDE_CEDAR, cedarCDE.toString());
    }

    @Test
    public void testParseCDEFromJsonNode_file()
            throws IOException, InvalidJsonPathException, DesignationNotFoundException,
            InvalidDatePrecisionException, UnsupportedDataTypeException {
        JsonNode node = mapper.readTree(ExampleNIHCDEs.fileCDE_NIH);
        ObjectNode cedarCDE = converter.convertCDEToCedar(node);
        assertEquals(ReferenceCEDARCDEs.fileCDE_CEDAR, cedarCDE.toString());
    }

    @Test
    public void testParseCDEFromJsonNode_externallyDefined()
            throws IOException, InvalidJsonPathException, DesignationNotFoundException,
            InvalidDatePrecisionException, UnsupportedDataTypeException {
        JsonNode node = mapper.readTree(ExampleNIHCDEs.externallyDefinedCDE_NIH);
        ObjectNode cedarCDE = converter.convertCDEToCedar(node);
        assertEquals(ReferenceCEDARCDEs.externallyDefinedCDE_CEDAR, cedarCDE.toString());
    }
}

