package com.URLShortener.URLShortener.security;

import com.URLShortener.URLShortener.domain.dto.TokenRequestDto;
import com.URLShortener.URLShortener.domain.entities.User;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    @Value("${jwt.secret.access}")
    private String SECRET_KEY_ACCESS;

    @Value("${jwt.secret.refresh}")
    private String SECRET_KEY_REFRESH;

    @Value("${jwt.secret.confirmation.email}")
    private String SECRET_KEY_CONFIRMATION_EMAIL;

    @Value("${jwt.secret.password.key}")
    private String SECRET_KEY_PASSWORD_KEY;

    @Value("${jwt.access.token.expiration}")
    private int ACCESS_TOKEN_EXPIRATION_MINUTES;

    @Value("${jwt.refresh.token.expiration}")
    private int REFRESH_TOKEN_EXPIRATION_MINUTES;

    public String generateToken(User user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY_ACCESS);
            String token = JWT.create()
                    .withIssuer("setup-auth")
                    .withSubject(user.getEmail())
                    .withClaim("role", user.getRole().toString())
                    .withExpiresAt(calculateAccessTokenExpiration())
                    .sign(algorithm);

            return token;
        } catch (JWTCreationException e) {
            throw new RuntimeException("Error generating token", e);
        }
    }

    public TokenRequestDto validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY_ACCESS);
            var decodedJWT = JWT.require(algorithm)
                    .withIssuer("setup-auth")
                    .build()
                    .verify(token);

            TokenRequestDto tokenRequest = new TokenRequestDto(
                    decodedJWT.getSubject(),
                    decodedJWT.getSubject(), // email é o subject
                    decodedJWT.getClaim("role").asString(),
                    decodedJWT.getExpiresAt().toString()
            );

            return tokenRequest;

        } catch (JWTVerificationException e) {
            throw new RuntimeException("Invalid or expired token", e);
        }
    }

    private Instant calculateAccessTokenExpiration() {
        return LocalDateTime.now().plusMinutes(ACCESS_TOKEN_EXPIRATION_MINUTES).toInstant(ZoneOffset.of("-03:00"));
    }

    private Instant calculateRefreshTokenExpiration() {
        return LocalDateTime.now().plusMinutes(REFRESH_TOKEN_EXPIRATION_MINUTES).toInstant(ZoneOffset.of("-03:00"));
    }

    private Instant calculateExpiration() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00")); // Brasilia time zone
    }

    public String generateEmailConfirmationToken(String email) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY_CONFIRMATION_EMAIL);
            String token = JWT.create()
                    .withIssuer("setup-auth")
                    .withSubject(email)
                    .withClaim("type", "email-confirmation")
                    .withExpiresAt(calculateExpiration())
                    .sign(algorithm);

            return token;
        } catch (JWTCreationException e) {
            throw new RuntimeException("Error generating email confirmation token", e);
        }
    }

    public TokenRequestDto validateEmailConfirmationToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY_CONFIRMATION_EMAIL);
            var decodedJWT = JWT.require(algorithm)
                    .withIssuer("setup-auth")
                    .withClaim("type", "email-confirmation")
                    .build()
                    .verify(token);

            TokenRequestDto tokenRequest = new TokenRequestDto(
                    decodedJWT.getSubject(),
                    decodedJWT.getSubject(),
                    decodedJWT.getClaim("type").asString(),
                    decodedJWT.getExpiresAt().toString()
            );

            return tokenRequest;

        } catch (JWTVerificationException e) {
            throw new RuntimeException("Invalid or expired email confirmation token", e);
        }
    }

    public String generateResetPasswordToken(String email) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY_PASSWORD_KEY);
            String token = JWT.create()
                    .withIssuer("setup-auth")
                    .withSubject(email)
                    .withClaim("type", "reset-password")
                    .withExpiresAt(calculateExpiration())
                    .sign(algorithm);

            return token;
        } catch (JWTCreationException e) {
            throw new RuntimeException("Error generating reset password token", e);
        }
    }

    public TokenRequestDto validateResetPasswordToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY_PASSWORD_KEY);
            var decodedJWT = JWT.require(algorithm)
                    .withIssuer("setup-auth")
                    .withClaim("type", "reset-password")
                    .build()
                    .verify(token);

            TokenRequestDto tokenRequest = new TokenRequestDto(
                    decodedJWT.getSubject(),
                    decodedJWT.getSubject(),
                    decodedJWT.getClaim("type").asString(),
                    decodedJWT.getExpiresAt().toString()
            );

            return tokenRequest;

        } catch (JWTVerificationException e) {
            throw new RuntimeException("Invalid or expired email confirmation token", e);
        }
    }

    public String generateRefreshToken(User user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY_REFRESH);
            String token = JWT.create()
                    .withIssuer("setup-auth")
                    .withSubject(user.getEmail())
                    .withClaim("type", "refresh")
                    .withExpiresAt(calculateRefreshTokenExpiration())
                    .sign(algorithm);

            return token;
        } catch (JWTCreationException e) {
            throw new RuntimeException("Error generating refresh token", e);
        }
    }

    public TokenRequestDto validateRefreshToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY_REFRESH);
            var decodedJWT = JWT.require(algorithm)
                    .withIssuer("setup-auth")
                    .withClaim("type", "refresh")
                    .build()
                    .verify(token);

            TokenRequestDto tokenRequest = new TokenRequestDto(
                    decodedJWT.getSubject(),
                    decodedJWT.getSubject(),
                    decodedJWT.getClaim("type").asString(),
                    decodedJWT.getExpiresAt().toString()
            );

            return tokenRequest;

        } catch (JWTVerificationException e) {
            throw new RuntimeException("Invalid or expired refresh token", e);
        }
    }

}