package com.URLShortener.URLShortener.exceptions;

public class OriginalUrlIsNotUrl extends RuntimeException {

    public OriginalUrlIsNotUrl() {
        super("The provided original URL is not a valid URL.");
    }

    public OriginalUrlIsNotUrl(String message) {
        super(message);
    }
}
