package org.metadatacenter.nih.ingestor.exceptions;

public class UnsupportedDataTypeException extends Exception {
    private final String datatype;

    public UnsupportedDataTypeException(String datatype) {
        this.datatype = datatype;
    }

    @Override
    public String getMessage() {
        return ("Currently no support for datatype: " + datatype);
    }
}
