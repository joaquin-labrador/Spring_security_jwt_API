package com.api.security.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ExceptionsAdvice {
    @ExceptionHandler(BadCredentials.class)
    public ResponseEntity<ApiErrorResponse> handleBadCredentials(BadCredentials ex) {
        ApiErrorResponse response = ApiErrorResponse.builder().message(ex.getMessage()).status(HttpStatus.BAD_REQUEST.value()).timestamp(LocalDateTime.now()).build();
        return ResponseEntity.status(401).body(response);
    }

    @ExceptionHandler(InternalServerError.class)
    public ResponseEntity<ApiErrorResponse> handleInternalServerError(InternalServerError ex) {
        ApiErrorResponse response = ApiErrorResponse.builder().message(ex.getMessage()).status(HttpStatus.INTERNAL_SERVER_ERROR.value()).timestamp(LocalDateTime.now()).build();
        return ResponseEntity.status(500).body(response);
    }
}
