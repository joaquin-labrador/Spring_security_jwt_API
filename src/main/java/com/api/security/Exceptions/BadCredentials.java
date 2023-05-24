package com.api.security.Exceptions;

public class BadCredentials extends RuntimeException {
    public BadCredentials(String message) {
        super(message);
    }
}
