package com.URLShortener.URLShortener.domain.dto;

import com.URLShortener.URLShortener.domain.entities.Role;

public record RegisterResponseDto(
        String email,
        Role role,
        String message
) {
}
