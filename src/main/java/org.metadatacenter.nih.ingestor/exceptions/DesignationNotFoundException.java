package org.metadatacenter.nih.ingestor.exceptions;

public class DesignationNotFoundException extends Exception {

    public DesignationNotFoundException () {
    }

    @Override
    public String getMessage() {
        return String.format("A CDE with no designations (labels) is not allowed. Inspect it.");
    }
}
