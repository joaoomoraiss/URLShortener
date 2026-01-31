package com.URLShortener.URLShortener.exceptions;

public class ErrorInSendEmailConfirmation extends RuntimeException {
    public ErrorInSendEmailConfirmation(String message) {
        super(message);
    }
}
