package org.metadatacenter.nih.ingestor.converter;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class CDEDesignationsTest {
    String testPrefLabel;
    ArrayList<String> testAltLabels;
    CDEDesignations designations;

    @Before
    public void setUp() {
        testPrefLabel = "preferredLabel";
        testAltLabels = new ArrayList<>();
        testAltLabels.add("altLabel1");
        testAltLabels.add("altLabel2");
        designations = new CDEDesignations(testPrefLabel, testAltLabels);
    }

    @Test
    public void testGetPreferredLabel() {
        String prefLabel = designations.getPreferredLabel();
        assertEquals(testPrefLabel, prefLabel);
    }

    @Test
    public void testGetAlternateLabels() {
        ArrayList<String> altLabels = designations.getAlternateLabels();
        assertEquals(testAltLabels, altLabels);
    }
}
