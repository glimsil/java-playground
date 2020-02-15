package com.glimsil.poc.elasticsearch.exception;

public class FullTextSearchException extends RuntimeException {
    public FullTextSearchException(String message) {
        super(message);
    }
    public FullTextSearchException(String message, Throwable t) {
        super(message, t);
    }
}
