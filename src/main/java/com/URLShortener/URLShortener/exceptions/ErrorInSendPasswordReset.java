package com.URLShortener.URLShortener.exceptions;

public class ErrorInSendPasswordReset extends RuntimeException {
    public ErrorInSendPasswordReset(String message) {
        super(message);
    }

    public ErrorInSendPasswordReset() {
        super("An error occurred while sending the password reset email.");
    }
}
