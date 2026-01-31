package com.URLShortener.URLShortener.controller;

import com.URLShortener.URLShortener.domain.dto.ErrorDto;
import com.URLShortener.URLShortener.exceptions.*;
import com.auth0.jwt.exceptions.TokenExpiredException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(OriginalUrlIsNotUrl.class)
    public ResponseEntity<ErrorDto> handlerOriginalUrlIsNotUrl(OriginalUrlIsNotUrl ex) {
        ErrorDto errorDto = new ErrorDto();
        errorDto.setError(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDto);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorDto> handleIllegalArgumentException(IllegalArgumentException ex) {
        ErrorDto errorDto = new ErrorDto();
        errorDto.setError(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDto);
    }

    @ExceptionHandler(InvalidShortCode.class)
    public ResponseEntity<ErrorDto> handleInvalidShortCode(InvalidShortCode ex) {
        ErrorDto errorDto = new ErrorDto();
        errorDto.setError(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDto);
    }

    @ExceptionHandler(InvalidIncrementId.class)
    public ResponseEntity<ErrorDto> handleInvalidIncrementId(InvalidIncrementId ex) {
        ErrorDto errorDto = new ErrorDto();
        errorDto.setError(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDto);
    }

    @ExceptionHandler(EmailExistException.class)
    public ResponseEntity<ErrorDto> handleEmailExistException(EmailExistException ex) {
        ErrorDto errorDTO = new ErrorDto(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDTO);
    }

    @ExceptionHandler(LoginFailedException.class)
    public ResponseEntity<ErrorDto> handleLoginFailedException(LoginFailedException ex) {
        ErrorDto errorDTO = new ErrorDto(ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorDTO);
    }

    @ExceptionHandler(ErrorInSendEmailConfirmation.class)
    public ResponseEntity<ErrorDto> handleErrorInSendEmailConfirmation(ErrorInSendEmailConfirmation ex) {
        ErrorDto errorDTO = new ErrorDto(ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDTO);
    }

    @ExceptionHandler(ErrorInSendPasswordReset.class)
    public ResponseEntity<ErrorDto> handleErrorInSendPasswordReset(ErrorInSendPasswordReset ex) {
        ErrorDto errorDTO = new ErrorDto(ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDTO);
    }

    @ExceptionHandler(RefreshTokenExpired.class)
    public ResponseEntity<ErrorDto> handleRefreshTokenExpired(RefreshTokenExpired ex) {
        ErrorDto errorDTO = new ErrorDto(ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorDTO);
    }

    @ExceptionHandler(RefreshTokenNotFoundException.class)
    public ResponseEntity<ErrorDto> handleRefreshTokenNotFoundException(RefreshTokenNotFoundException ex) {
        ErrorDto errorDTO = new ErrorDto(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);
    }

    @ExceptionHandler(RevokedRefreshTokenException.class)
    public ResponseEntity<ErrorDto> handleRevokedRefreshTokenException(RevokedRefreshTokenException ex) {
        ErrorDto errorDTO = new ErrorDto(ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorDTO);
    }

    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<ErrorDto> handleTokenExpiredException(TokenExpiredException ex) {
        ErrorDto errorDTO = new ErrorDto("Token has expired");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorDTO);
    }
}
