package com.URLShortener.URLShortener.services;

import com.URLShortener.URLShortener.domain.dto.*;

public interface AuthService {
    RegisterResponseDto register(RegisterRequestDto request);

    AuthTokenDto login(LoginRequestDto request);

    void confirmEmail(String token);

    void sendEmailConfirmation(String email);

    void sendResetPassword(String email);

    void resetPassword(ResetPasswordRequestDto request);

    AuthTokenDto refreshToken(String refreshToken);

    void logout(RefreshTokenRequestDto request);
}
