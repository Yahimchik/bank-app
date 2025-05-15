package com.example.bankapp.service;

import java.util.UUID;

public interface EmailDataService {
    void addEmail(UUID userId, String email);
    void deleteEmail(UUID userId, String email);
    void updateEmail(UUID userId, String oldEmail, String newEmail);
}

