package com.URLShortener.URLShortener.domain.dto;

public record AuthTokenDto(
        String accessToken,
        String refreshToken
) {
}
