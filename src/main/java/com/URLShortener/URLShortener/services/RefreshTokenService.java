package com.URLShortener.URLShortener.services;

import com.URLShortener.URLShortener.domain.entities.RefreshToken;
import com.URLShortener.URLShortener.domain.entities.User;

public interface RefreshTokenService {
    String createRefreshToken(User user);
    RefreshToken validateRefreshToken(String token);
    void revokeToken(String token);
    void revokeAllUserTokens(User user);
    void cleanupExpiredTokens();
    void cleanupOldTokens();
}
