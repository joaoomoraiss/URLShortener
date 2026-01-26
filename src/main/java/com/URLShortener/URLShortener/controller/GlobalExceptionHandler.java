package com.URLShortener.URLShortener.controller;

import com.URLShortener.URLShortener.domain.dto.ErrorDto;
import com.URLShortener.URLShortener.exceptions.InvalidIncrementId;
import com.URLShortener.URLShortener.exceptions.InvalidShortCode;
import com.URLShortener.URLShortener.exceptions.OriginalUrlIsNotUrl;
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
}
