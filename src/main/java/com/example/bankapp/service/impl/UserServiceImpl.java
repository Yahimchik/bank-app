package com.example.bankapp.service.impl;

import com.example.bankapp.dto.user.UserFilterDTO;
import com.example.bankapp.dto.user.UserResponseDTO;
import com.example.bankapp.entities.User;
import com.example.bankapp.mapper.UserMapper;
import com.example.bankapp.repo.EmailDataRepository;
import com.example.bankapp.repo.UserRepository;
import com.example.bankapp.service.UserService;
import com.example.bankapp.service.exception.user.UserNotFoundException;
import com.example.bankapp.repo.specification.UserSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final EmailDataRepository emailDataRepository;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    @Cacheable(value = "userCache", key = "#filter + '_' + #pageRequest.pageNumber + '_' + #pageRequest.pageSize")
    public List<UserResponseDTO> viewAllUsers(UserFilterDTO filter, PageRequest pageRequest) {
        log.info("Fetching all users from the database");

        Pageable pageable = PageRequest.of(pageRequest.getPageNumber(), pageRequest.getPageSize());
        Page<User> cardsPage = userRepository.findAll(UserSpecification.withFilters(filter), pageable);

        log.info("Found {} users", cardsPage.getTotalElements());
        return cardsPage.getContent().stream()
                .map(userMapper::convertToUserResponse)
                .collect(Collectors.toList());
    }

    @Cacheable(value = "userByEmailCache", key = "#email")
    public UserResponseDTO getUserByEmail(String email) {
        log.info("Fetching user by email: {}", email);

        User user = emailDataRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + email))
                .getUser();

        return userMapper.convertToUserResponse(user);
    }

    @Override
    public UserResponseDTO getUserById(UUID id) {
        return userMapper.convertToUserResponse(userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with ID '" + id + "' not found")));
    }
}
