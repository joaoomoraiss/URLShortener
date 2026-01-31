package com.URLShortener.URLShortener.services;

import com.URLShortener.URLShortener.domain.dto.MailDto;
import jakarta.mail.MessagingException;

public interface MailService {
    void sendEmail(MailDto mailDto) throws MessagingException;

    void sendEmailConfirmation(String email, String token) throws MessagingException;

    void sendEmailResetPassword(String email, String token) throws MessagingException;
}
