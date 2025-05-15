package com.example.bankapp.service;


import com.example.bankapp.dto.user.UserFilterDTO;
import com.example.bankapp.dto.user.UserResponseDTO;
import com.example.bankapp.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.UUID;

public interface UserService {
    List<UserResponseDTO> viewAllUsers(UserFilterDTO filter, PageRequest pageRequest);

    UserResponseDTO getUserByEmail(String email);

    UserResponseDTO getUserById(UUID id);
}
