package com.URLShortener.URLShortener.exceptions;

public class InvalidIncrementId extends RuntimeException {
    public InvalidIncrementId(String message) {
        super(message);
    }

    public InvalidIncrementId() {
        super("Failed to generate next ID.");
    }
}
