package com.URLShortener.URLShortener.domain;

public class RedirectUrlRequest {
    private String shortCode;

    public RedirectUrlRequest() {
    }

    public RedirectUrlRequest(String shortCode) {
        this.shortCode = shortCode;
    }

    public String getShortCode() {
        return shortCode;
    }

    public void setShortCode(String shortCode) {
        this.shortCode = shortCode;
    }
}
