package com.api.security.Config;

import com.api.security.Exceptions.ApiErrorResponse;
import com.api.security.Exceptions.BadCredentials;
import com.api.security.Exceptions.InternalServerError;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ExceptionsAdvice {
    @ExceptionHandler(BadCredentials.class)
    public ResponseEntity<ApiErrorResponse> handleBadCredentials(BadCredentials ex) {
        ApiErrorResponse response = ApiErrorResponse.builder().message(ex.getMessage()).status(401).timestamp(LocalDateTime.now()).build();
        return ResponseEntity.status(401).body(response);
    }

    @ExceptionHandler(InternalServerError.class)
    public ResponseEntity<ApiErrorResponse> handleInternalServerError(InternalServerError ex) {
        ApiErrorResponse response = ApiErrorResponse.builder().message(ex.getMessage()).status(500).timestamp(LocalDateTime.now()).build();
        return ResponseEntity.status(500).body(response);
    }
}
