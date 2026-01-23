package com.URLShortener.URLShortener.domain.dto;

public record UrlDto(
        String shortCode,
        String originalUrl
) {
}
