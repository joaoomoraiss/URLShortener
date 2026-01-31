package com.URLShortener.URLShortener.exceptions;

public class RefreshTokenNotFoundException extends RuntimeException {
    public RefreshTokenNotFoundException(String message) {
        super(message);
    }

    public RefreshTokenNotFoundException() {
        super("Refresh token not found.");
    }
}
