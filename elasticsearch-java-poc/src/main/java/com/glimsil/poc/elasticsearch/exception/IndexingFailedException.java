package com.glimsil.poc.elasticsearch.exception;

public class IndexingFailedException extends RuntimeException {
    public IndexingFailedException(String message) {
        super(message);
    }
    public IndexingFailedException(String message, Throwable t) {
        super(message, t);
    }
}
