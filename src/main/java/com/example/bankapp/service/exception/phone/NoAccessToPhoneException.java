package com.example.bankapp.service.exception.phone;

public class NoAccessToPhoneException extends RuntimeException {
    public NoAccessToPhoneException(String message) {
        super(message);
    }
}
