package com.URLShortener.URLShortener.services.impl;

import com.URLShortener.URLShortener.domain.dto.*;
import com.URLShortener.URLShortener.domain.entities.RefreshToken;
import com.URLShortener.URLShortener.domain.entities.User;
import com.URLShortener.URLShortener.exceptions.*;
import com.URLShortener.URLShortener.repositories.jpa.RefreshTokenRepository;
import com.URLShortener.URLShortener.repositories.jpa.UserRepository;
import com.URLShortener.URLShortener.security.TokenService;
import com.URLShortener.URLShortener.services.AuthService;
import com.URLShortener.URLShortener.services.MailService;
import com.URLShortener.URLShortener.services.RefreshTokenService;
import com.URLShortener.URLShortener.services.UserService;
import jakarta.mail.MessagingException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final MailService mailService;
    private final RefreshTokenService refreshTokenService;
    private final RefreshTokenRepository refreshTokenRepository;

    public AuthServiceImpl(UserRepository userRepository, UserService userService, AuthenticationManager authenticationManager, TokenService tokenService, MailService mailService, RefreshTokenService refreshTokenService, RefreshTokenRepository refreshTokenRepository) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
        this.mailService = mailService;
        this.refreshTokenService = refreshTokenService;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Transactional
    public RegisterResponseDto register(RegisterRequestDto request) {

        // Check if user already exists
        if (userRepository.findByEmail(request.email()) != null) {
            // TO-DO: custom exception
            throw new EmailExistException("Email already registered");
        }

        // Create new user
        User newUser = userService.createUser(request);
        User savedUser = userService.saveUser(newUser);

        // Generate email confirmation token
        String confirmationToken = tokenService.generateEmailConfirmationToken(savedUser.getEmail());

        // Send confirmation email
        try {
            mailService.sendEmailConfirmation(savedUser.getEmail(), confirmationToken);
        } catch (Exception e) {
            throw new ErrorInSendEmailConfirmation("Error sending confirmation email" + e);
        }

        return new RegisterResponseDto(
                savedUser.getEmail(),
                savedUser.getRole(),
                "User registered successfully. Please check your email to confirm your account."
        );
    }

    public AuthTokenDto login(LoginRequestDto request) {
        var user = new UsernamePasswordAuthenticationToken(request.email(), request.password());
        var auth = authenticationManager.authenticate(user);

        if (auth.getPrincipal() == null) {
            throw new LoginFailedException("Authentication failed");
        }

        User authenticatedUser = (User) auth.getPrincipal();
        String accessToken = tokenService.generateToken(authenticatedUser);

        // create and save refresh token
        String refreshToken = refreshTokenService.createRefreshToken(authenticatedUser);

        return new AuthTokenDto(accessToken, refreshToken);
    }

    public void confirmEmail(String token) {
        //validate token
        TokenRequestDto tokenRequest = tokenService.validateEmailConfirmationToken(token);

        //get user
        User user = (User) userRepository.findByEmail(tokenRequest.email());
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        if (user.isVerified()) return;

        //confirm email
        user.setVerified(true);
        userRepository.save(user);
    }

    public void sendEmailConfirmation(String email) {

        // generate token
        String emailConfirmationToken = tokenService.generateEmailConfirmationToken(email);

        //send email
        try {
            mailService.sendEmailConfirmation(email, emailConfirmationToken);
        } catch (MessagingException e) {
            throw new ErrorInSendEmailConfirmation("Error sending email confirmation " + e);
        }
    }

    public void sendResetPassword(String email) {

        //get token
        String resetPasswordToken = tokenService.generateResetPasswordToken(email);

        //send email
        try {
            mailService.sendEmailResetPassword(email, resetPasswordToken);
        } catch (MessagingException e) {
            throw new ErrorInSendPasswordReset("Error sending reset password " + e);
        }
    }

    public void resetPassword(ResetPasswordRequestDto request) {

        //validate token
        TokenRequestDto tokenData = tokenService.validateResetPasswordToken(request.token());

        //get user
        User user = (User) userRepository.findByEmail(tokenData.email());
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        //reset password
        userService.updatePassword(user, request.newPassword());

    }

    @Transactional
    public AuthTokenDto refreshToken(String refreshToken) {

        var refreshTokenEntity = refreshTokenService.validateRefreshToken(refreshToken);

        User user = refreshTokenEntity.getUser();

        // revoke old refresh token
        refreshTokenService.revokeToken(refreshToken);

        // Ggenerate new tokens
        String newAccessToken = tokenService.generateToken(user);
        String newRefreshToken = refreshTokenService.createRefreshToken(user);

        return new AuthTokenDto(newAccessToken, newRefreshToken);
    }

    @Transactional
    public void logout(RefreshTokenRequestDto request) {
        RefreshToken token = refreshTokenRepository.findByToken(request.refreshToken())
                .orElseThrow(() -> new RefreshTokenNotFoundException("Refresh token not found"));

        if (token.isRevoked()) {
            throw new RevokedRefreshTokenException("Refresh token is revoked");
        }

        refreshTokenService.revokeToken(request.refreshToken());
    }

}
