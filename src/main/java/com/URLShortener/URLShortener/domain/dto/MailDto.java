package com.URLShortener.URLShortener.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public record MailDto(
    String from,
    String to,
    String subject,
    String body
    ) {
}
