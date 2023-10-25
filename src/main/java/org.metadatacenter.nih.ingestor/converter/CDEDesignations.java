package org.metadatacenter.nih.ingestor.converter;

import java.util.ArrayList;

public class CDEDesignations {

    private String preferredLabel;
    private ArrayList<String> alternateLabels;

    public CDEDesignations(String preferredLabel, ArrayList<String> alternateLabels) {
        this.preferredLabel = preferredLabel;
        this.alternateLabels = alternateLabels;
    }

    public String getPreferredLabel() {
        return preferredLabel;
    }

    public ArrayList<String> getAlternateLabels() {
        return alternateLabels;
    }
}
