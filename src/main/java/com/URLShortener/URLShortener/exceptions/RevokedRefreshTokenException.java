package com.URLShortener.URLShortener.exceptions;

public class RevokedRefreshTokenException extends RuntimeException {
    public RevokedRefreshTokenException(String message) {
        super(message);
    }

    public RevokedRefreshTokenException() {
        super("The provided refresh token has been revoked.");
    }
}
