package com.example.bankapp.service.exception.email;

public class NoAccessToEmailException extends RuntimeException {
    public NoAccessToEmailException(String message) {
        super(message);
    }
}
