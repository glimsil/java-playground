package com.glimsil.poc.okhttp.exception;

public class RetriesExceededException extends RuntimeException {
    public RetriesExceededException(String message) {
        super(message);
    }
}
