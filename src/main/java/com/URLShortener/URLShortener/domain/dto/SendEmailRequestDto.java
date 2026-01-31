package com.URLShortener.URLShortener.domain.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record SendEmailRequestDto(
        @NotBlank(message = "Email cant be blank")
        @Email(message = "Email deve ser válido")
        String email
) {
}
