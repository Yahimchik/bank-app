package com.example.bankapp.service;

import java.util.UUID;

public interface PhoneDataService {
    void addPhone(UUID userId, String phone);
    void deletePhone(UUID userId, String phone);
    void updatePhone(UUID userId, String oldPhone, String newPhone);
}

