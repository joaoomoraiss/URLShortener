package com.URLShortener.URLShortener.domain.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record TokenRequestDto(
        @NotBlank(message = "Sub cant be blank")
        String sub,
        @NotBlank(message = "Email cant be blank")
        @Email(message = "Email must be valid")
        String email,

        @NotBlank(message = "Type cant be blank")
        String type,

        @NotBlank(message = "Expiration cant be blank")
        String expiration
) {
}
