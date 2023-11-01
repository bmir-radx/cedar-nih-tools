package org.metadatacenter.nih.ingestor.exceptions;

public class InvalidDatePrecisionException extends Exception {

    private final String datePrecision;

    public InvalidDatePrecisionException(String datePrecision) {
        this.datePrecision = datePrecision;
    }

    @Override
    public String getMessage() {
        return (datePrecision + " is an invalid precision for NIH CDE Date fields (but maybe we need to add it).");
    }
}
