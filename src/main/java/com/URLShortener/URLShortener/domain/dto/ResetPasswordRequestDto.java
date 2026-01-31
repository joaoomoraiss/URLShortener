package com.URLShortener.URLShortener.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ResetPasswordRequestDto(
        @NotBlank(message = "Token cant be blank")
        String token,

        @NotBlank(message = "new password cant be blank")
        @Size(min = 6, message = "error in set new password")
        String newPassword
) {
}
