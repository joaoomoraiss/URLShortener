package com.URLShortener.URLShortener.domain.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequestDto(
        @NotBlank(message = "Email cant be blank")
        @Email(message = "Email must be valid")
        String email,

        @NotBlank(message = "Password cant be blank")
        String password
) {
}
