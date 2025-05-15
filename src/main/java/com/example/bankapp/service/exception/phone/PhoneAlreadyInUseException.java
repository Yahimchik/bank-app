package com.example.bankapp.service.exception.phone;

public class PhoneAlreadyInUseException extends RuntimeException {
    public PhoneAlreadyInUseException(String message) {
        super(message);
    }
}
