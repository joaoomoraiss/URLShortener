package com.URLShortener.URLShortener.controller;

import com.URLShortener.URLShortener.domain.dto.*;
import com.URLShortener.URLShortener.services.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authenticationService;

    @Value("${cookie.secure}")
    private boolean cookieSecure;

    public AuthController(AuthService authenticationService) {
        this.authenticationService = authenticationService;
    }


    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(
            @RequestBody @Valid LoginRequestDto loginRequestDto,
            HttpServletResponse response
    ) {
        try {
            AuthTokenDto tokens = authenticationService.login(loginRequestDto);

            // cookie
            ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", tokens.refreshToken())
                    .httpOnly(true)
                    .secure(true)
                    .sameSite("Strict")
                    .path("/api/auth/refresh-token")
                    .maxAge(Duration.ofDays(30)) // 7 days
                    .build();

            response.addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());

            return ResponseEntity.ok(new LoginResponseDto(tokens.accessToken()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid RegisterRequestDto registerRequest) {
        try {
            RegisterResponseDto response = authenticationService.register(registerRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/confirm-email/{token}")
    public ResponseEntity<String> confirmEmail(@PathVariable @Valid String token) {
        try {
            authenticationService.confirmEmail(token);
            return ResponseEntity.ok("Email confirmed successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/send-email-confirmation")
    public ResponseEntity<String> sendEmailConfirmation(@RequestBody @Valid SendEmailRequestDto request) {
        try {
            authenticationService.sendEmailConfirmation(request.email());
            return ResponseEntity.ok("Email confirmed successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/send-reset-password")
    public ResponseEntity<String> sendResetPassword(@RequestBody @Valid SendEmailRequestDto request) {
        try {
            authenticationService.sendResetPassword(request.email());
            return ResponseEntity.ok("Password reset email sent successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody @Valid ResetPasswordRequestDto request) {
        try {
            authenticationService.resetPassword(request);
            return ResponseEntity.ok("Password reset successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<LoginResponseDto> refreshToken(
            @RequestBody @Valid RefreshTokenRequestDto request,
            HttpServletResponse response
    ) {
        try {
            AuthTokenDto tokens = authenticationService.refreshToken(request.refreshToken());

            // new refreshToken cookie (secure=false for localhost, true for production)
            ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", tokens.refreshToken())
                    .httpOnly(true)
                    .secure(cookieSecure) // false in localhost (HTTP), true in production (HTTPS)
                    .sameSite("Strict")
                    .path("/api/auth/refresh-token")
                    .maxAge(Duration.ofDays(30))
                    .build();

            response.addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());

            return ResponseEntity.ok(new LoginResponseDto(tokens.accessToken()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestBody @Valid RefreshTokenRequestDto request) {
        // Invalidate the refresh token cookie
        ResponseCookie deleteCookie = ResponseCookie.from("refreshToken", "")
                .httpOnly(true)
                .secure(cookieSecure)
                .sameSite("Strict")
                .path("/api/auth/refresh-token")
                .maxAge(0) // Expire immediately
                .build();

        authenticationService.logout(request);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, deleteCookie.toString())
                .body("Logged out successfully");
    }
}
