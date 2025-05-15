package com.example.bankapp.service.exception.user;

public class UserAuthenticationProcessingException extends RuntimeException {
    public UserAuthenticationProcessingException(String message) {
        super(message);
    }
}
