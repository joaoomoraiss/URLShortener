package com.URLShortener.URLShortener.repositories.jpa;

import com.URLShortener.URLShortener.domain.entities.RefreshToken;
import com.URLShortener.URLShortener.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {

    Optional<RefreshToken> findByToken(String token);

    void deleteByUser(User user);

    void deleteByToken(String token);

    @Modifying
    @Query("DELETE FROM RefreshToken rt WHERE rt.expiryDate < :now")
    void deleteExpiredTokens(Instant now);

    @Modifying
    @Query("DELETE FROM RefreshToken rt WHERE rt.revoked = true AND rt.expiryDate < :now")
    void deleteRevokedAndExpiredTokens(Instant now);
}
