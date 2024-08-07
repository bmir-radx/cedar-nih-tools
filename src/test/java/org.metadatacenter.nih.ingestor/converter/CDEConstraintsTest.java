package org.metadatacenter.nih.ingestor.converter;

import org.junit.Before;
import org.junit.Test;
import org.metadatacenter.artifacts.model.core.fields.XsdNumericDatatype;
import org.metadatacenter.nih.ingestor.constants.JsonDatePrecisions;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CDEConstraintsTest {

    CDEConstraints.CDEConstraintsBuilder builder;

    @Before
    public void setUp() {
        builder = new CDEConstraints.CDEConstraintsBuilder();
    }

    @Test
    public void testBuilder() {
        String datePrecision = JsonDatePrecisions.DAY;
        ArrayList<String> permissibleValues = new ArrayList<>();
        permissibleValues.add("permissibleValue1");
        permissibleValues.add("permissibleValue2");
        Integer minLength = 1;
        Integer maxLength = 2;
        Integer minValue = 0;
        Integer maxValue = 1;
        Integer numericPrecision = 0;
        CDEConstraints constraints = builder
                .withPermissibleValues(permissibleValues)
                .withDatePrecision(datePrecision)
                .withMinValue(minValue)
                .withMaxValue(maxValue)
                .withMinLength(minLength)
                .withMaxLength(maxLength)
                .withNumericPrecision(numericPrecision)
                .build();
        assertTrue(constraints.hasDatePrecision());
        assertTrue(constraints.hasPermissibleValues());
        assertTrue(constraints.hasMinLength());
        assertTrue(constraints.hasMaxLength());
        assertTrue(constraints.hasMinValue());
        assertTrue(constraints.hasMaxValue());
        assertTrue(constraints.hasNumericPrecision());
        assertTrue(constraints.hasNumericType());
    }

    @Test
    public void testBuilderNone() {
        CDEConstraints constraints = builder.build();
        assertFalse(constraints.hasDatePrecision());
        assertFalse(constraints.hasPermissibleValues());
        assertFalse(constraints.hasMinLength());
        assertFalse(constraints.hasMaxLength());
        assertFalse(constraints.hasMinValue());
        assertFalse(constraints.hasMaxValue());
        assertFalse(constraints.hasNumericPrecision());
        assertTrue(constraints.hasNumericType());
        // Numeric type defaults to Integer because it is a required
        // field for Numeric Fields in CEDAR.
    }

    @Test
    public void testGetDatePrecision() {
        String datePrecision = JsonDatePrecisions.DAY;
        CDEConstraints constraints = builder
                .withDatePrecision(datePrecision)
                .build();
        assertTrue(constraints.hasDatePrecision());
        assertEquals(datePrecision, constraints.getDatePrecision());
    }

    @Test
    public void testGetPermissibleValues() {
        ArrayList<String> permissibleValues = new ArrayList<>();
        permissibleValues.add("permissibleValue1");
        permissibleValues.add("permissibleValue2");
        CDEConstraints constraints = builder
                .withPermissibleValues(permissibleValues)
                .build();
        assertTrue(constraints.hasPermissibleValues());
        assertEquals(permissibleValues, constraints.getPermissibleValues());
    }

    @Test
    public void testGetPermissibleValuesEmpty() {
        ArrayList<String> permissibleValues = new ArrayList<>();
        CDEConstraints constraints = builder
                .withPermissibleValues(permissibleValues)
                .build();
        assertFalse(constraints.hasPermissibleValues());
    }

    @Test
    public void testGetMinLength() {
        Integer minLength = 1;
        CDEConstraints constraints = builder
                .withMinLength(minLength)
                .build();
        assertTrue(constraints.hasMinLength());
        assertEquals(minLength, constraints.getMinLength());
    }

    @Test
    public void testGetMaxLength() {
        Integer maxLength = 2;
        CDEConstraints constraints = builder
                .withMaxLength(maxLength)
                .build();
        assertTrue(constraints.hasMaxLength());
        assertEquals(maxLength, constraints.getMaxLength());
    }

    @Test
    public void testGetXsdNumericDataType_fromPrecision_integer() {
        Integer numericPrecision = 0;
        CDEConstraints constraints = builder
                .withNumericPrecision(numericPrecision)
                .build();
        assertTrue(constraints.hasNumericType());
        assertEquals(XsdNumericDatatype.INTEGER, constraints.getNumericType());
    }

    @Test
    public void testGetXsdNumericDataType_fromPrecision_decimal() {
        Integer numericPrecision = 1;
        CDEConstraints constraints = builder
                .withNumericPrecision(numericPrecision)
                .build();
        assertTrue(constraints.hasNumericType());
        assertEquals(XsdNumericDatatype.DECIMAL, constraints.getNumericType());
    }

    @Test
    public void testGetXsdNumericDataType_fromLimits_integer() {
        Number minValue = 1;
        Number maxValue = 15;
        CDEConstraints constraints = builder
                .withMinValue(minValue)
                .withMaxValue(maxValue)
                .build();
        assertTrue(constraints.hasNumericType());
        assertEquals(XsdNumericDatatype.INTEGER, constraints.getNumericType());
    }

    @Test
    public void testGetXsdNumericDataType_fromLimits_decimal() {
        Number minValue = 1;
        Number maxValue = 15.1;
        CDEConstraints constraints = builder
                .withMinValue(minValue)
                .withMaxValue(maxValue)
                .build();
        assertTrue(constraints.hasNumericType());
        assertEquals(XsdNumericDatatype.DECIMAL, constraints.getNumericType());
    }

    @Test
    public void testGetXsdNumericDataType_fromLimitsAndPrecision_integer() {
        Integer precision = 0;
        Number minValue = 1;
        Number maxValue = 15.5;
        CDEConstraints constraints = builder
                .withMinValue(minValue)
                .withMaxValue(maxValue)
                .withNumericPrecision(precision)
                .build();
        assertTrue(constraints.hasNumericType());
        assertEquals(XsdNumericDatatype.INTEGER, constraints.getNumericType());
    }

    @Test
    public void testGetXsdNumericDataType_fromLimitsAndPrecision_decimal() {
        Integer precision = 1;
        Number minValue = 1;
        Number maxValue = 15.0;
        CDEConstraints constraints = builder
                .withMinValue(minValue)
                .withMaxValue(maxValue)
                .withNumericPrecision(precision)
                .build();
        assertTrue(constraints.hasNumericType());
        assertEquals(XsdNumericDatatype.DECIMAL, constraints.getNumericType());
    }

    @Test
    public void testGetNumericPrecision() {
        Integer numericPrecision = 0;
        CDEConstraints constraints = builder
                .withNumericPrecision(numericPrecision)
                .build();
        assertTrue(constraints.hasNumericPrecision());
        assertEquals(numericPrecision, constraints.getNumericPrecision());
    }

    @Test
    public void testGetMinValue() {
        Integer minValue = 1;
        CDEConstraints constraints = builder
                .withMinValue(minValue)
                .build();
        assertTrue(constraints.hasMinValue());
        assertEquals(minValue, constraints.getMinValue());
    }

    @Test
    public void testGetMaxValue() {
        Integer maxValue = 1;
        CDEConstraints constraints = builder
                .withMaxValue(maxValue)
                .build();
        assertTrue(constraints.hasMaxValue());
        assertEquals(maxValue, constraints.getMaxValue());
    }
}
