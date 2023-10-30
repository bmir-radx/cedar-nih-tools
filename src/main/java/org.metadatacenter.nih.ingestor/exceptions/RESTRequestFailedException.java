package org.metadatacenter.nih.ingestor.exceptions;

public class RESTRequestFailedException extends Exception {

    private String request;
    private Integer responseCode;

    public RESTRequestFailedException(String request, Integer responseCode) {
        this.request = request;
        this.responseCode = responseCode;
    }

    @Override
    public String getMessage() {
        return (request + " request failed with response code " + responseCode);
    }
}
