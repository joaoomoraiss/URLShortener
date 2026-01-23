package com.URLShortener.URLShortener.domain;

public class CreateUrlRequest {

    private String originalUrl;

    public CreateUrlRequest() {
    }

    public CreateUrlRequest(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }
}
