package com.glimsil.poc.okhttp.exception;

public class FallbackMethodFailedException extends RuntimeException {
    public FallbackMethodFailedException(String message) {
        super(message);
    }
    public FallbackMethodFailedException(String message, Throwable t) {
        super(message, t);
    }
}
