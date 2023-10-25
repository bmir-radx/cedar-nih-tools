package org.metadatacenter.nih.ingestor.exceptions;

public class InvalidJsonPathException extends Exception {

    private final String path;

    public InvalidJsonPathException(String path) {
        this.path = path;
    }

    @Override
    public String getMessage() {
        return String.format("\"%s\" is not a valid path in the Json tree.", path);
    }
}
