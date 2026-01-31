package com.URLShortener.URLShortener.exceptions;

public class RefreshTokenExpired extends RuntimeException {
    public RefreshTokenExpired(String message) {
        super(message);
    }

    public RefreshTokenExpired() {
        super("The provided refresh token has expired.");
    }
}
