package com.URLShortener.URLShortener.exceptions;

public class LoginFailedException extends RuntimeException {
    public LoginFailedException(String message) {
        super(message);
    }

    public LoginFailedException() {
        super("Login failed due to incorrect email or password.");
    }
}
