package com.URLShortener.URLShortener.services.impl;

import com.URLShortener.URLShortener.domain.entities.RefreshToken;
import com.URLShortener.URLShortener.domain.entities.User;
import com.URLShortener.URLShortener.exceptions.RefreshTokenExpired;
import com.URLShortener.URLShortener.exceptions.RefreshTokenNotFoundException;
import com.URLShortener.URLShortener.exceptions.RevokedRefreshTokenException;
import com.URLShortener.URLShortener.repositories.jpa.RefreshTokenRepository;
import com.URLShortener.URLShortener.security.TokenService;
import com.URLShortener.URLShortener.services.RefreshTokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private static final Logger logger = LoggerFactory.getLogger(RefreshTokenService.class);

    private final RefreshTokenRepository refreshTokenRepository;
    private final TokenService tokenService;

    public RefreshTokenServiceImpl(RefreshTokenRepository refreshTokenRepository, TokenService tokenService) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.tokenService = tokenService;
    }

    @Transactional
    public String createRefreshToken(User user) {
        // Gera o token JWT
        String tokenValue = tokenService.generateRefreshToken(user);

        // Cria a entidade no banco
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(tokenValue);
        refreshToken.setUser(user);
        refreshToken.setExpiryDate(Instant.now().plus(30, ChronoUnit.DAYS));
        refreshToken.setRevoked(false);

        refreshTokenRepository.save(refreshToken);

        return tokenValue;
    }

    @Transactional
    public RefreshToken validateRefreshToken(String token) {
        Optional<RefreshToken> refreshTokenOpt = refreshTokenRepository.findByToken(token);

        if (refreshTokenOpt.isEmpty()) {
            throw new RefreshTokenNotFoundException();
        }

        RefreshToken refreshToken = refreshTokenOpt.get();

        if (refreshToken.isRevoked()) {
            throw new RevokedRefreshTokenException();
        }

        if (refreshToken.getExpiryDate().isBefore(Instant.now())) {
            throw new RefreshTokenExpired();
        }

        return refreshToken;
    }

    @Transactional
    public void revokeToken(String token) {
        Optional<RefreshToken> refreshTokenOpt = refreshTokenRepository.findByToken(token);
        refreshTokenOpt.ifPresent(rt -> {
            rt.setRevoked(true);
            refreshTokenRepository.save(rt);
        });
    }

    @Transactional
    public void revokeAllUserTokens(User user) {
        refreshTokenRepository.deleteByUser(user);
    }

    /**
     * Scheduled task that runs every day at 3 AM to clean up expired tokens
     * This prevents the database from filling up with old tokens
     */
    @Scheduled(cron = "0 0 3 * * *") // Runs at 3:00 AM every day
    @Transactional
    public void cleanupExpiredTokens() {
        logger.info("Starting scheduled cleanup of expired refresh tokens");
        try {
            refreshTokenRepository.deleteExpiredTokens(Instant.now());
            logger.info("Successfully cleaned up expired refresh tokens");
        } catch (Exception e) {
            logger.error("Error during cleanup of expired refresh tokens", e);
        }
    }

    /**
     * Manual cleanup method that can be called on-demand
     * Removes both expired and revoked tokens
     */
    @Transactional
    public void cleanupOldTokens() {
        logger.info("Manual cleanup of old refresh tokens");
        refreshTokenRepository.deleteRevokedAndExpiredTokens(Instant.now());
    }
}
