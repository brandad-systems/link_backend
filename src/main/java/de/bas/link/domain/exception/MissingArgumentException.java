package de.bas.link.domain.exception;

public class MissingArgumentException extends RuntimeException{
    public MissingArgumentException(String argumentName) {
        super(String.format("Argument '%s' is required.", argumentName));
    }
}