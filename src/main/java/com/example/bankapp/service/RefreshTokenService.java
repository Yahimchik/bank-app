package com.example.bankapp.service;


import com.example.bankapp.entities.RefreshToken;
import com.example.bankapp.entities.User;

public interface RefreshTokenService {
    RefreshToken save(RefreshToken token);

    RefreshToken findByUserAndIp(User user, String ip);

    RefreshToken updateRefreshToken(RefreshToken token, String newToken);

    void deleteByUserAndIp(User user, String ip);
}
