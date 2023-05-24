package com.api.security.Exceptions;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
public class ApiErrorResponse {
    LocalDateTime timestamp;  //2021-08-18T17:00:00.000
    private String message;
    private int status;

}
