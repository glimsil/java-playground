package com.glimsil.poc.okhttp.exception;

public class BodyParsingException extends RuntimeException {
    public BodyParsingException(String message) {
        super(message);
    }
    public BodyParsingException(String message, Throwable t) {
        super(message, t);
    }
}
