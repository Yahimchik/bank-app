package com.example.bankapp.service.exception.transaction;

public class TransferToSameUserException extends RuntimeException {
    public TransferToSameUserException(String message) {
        super(message);
    }
}
