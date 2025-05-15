package com.example.bankapp.service;


import com.example.bankapp.dto.auth.AuthenticationDTO;
import com.example.bankapp.dto.auth.JwtResponseDTO;
import com.example.bankapp.dto.auth.RefreshJwtRequestDTO;

public interface AuthenticationService {
    JwtResponseDTO authenticate(AuthenticationDTO authenticationDto);

    JwtResponseDTO recreateToken(RefreshJwtRequestDTO refreshJwtRequestDto);

    void logout();
}
