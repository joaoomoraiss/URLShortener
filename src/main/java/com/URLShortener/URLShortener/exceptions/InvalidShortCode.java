package com.URLShortener.URLShortener.exceptions;

public class InvalidShortCode extends RuntimeException {
    public InvalidShortCode(String message) {
        super(message);
    }
    public InvalidShortCode() {
        super("The provided short code is invalid.");
    }
}
