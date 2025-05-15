package com.example.bankapp.service.impl;

import com.example.bankapp.dto.auth.AuthenticationDTO;
import com.example.bankapp.dto.auth.JwtResponseDTO;
import com.example.bankapp.dto.auth.RefreshJwtRequestDTO;
import com.example.bankapp.dto.user.UserResponseDTO;
import com.example.bankapp.entities.RefreshToken;
import com.example.bankapp.entities.User;
import com.example.bankapp.mapper.UserMapper;
import com.example.bankapp.repo.RefreshTokenRepository;
import com.example.bankapp.repo.UserRepository;
import com.example.bankapp.security.jwt.JwtTokenProvider;
import com.example.bankapp.service.AuthenticationService;
import com.example.bankapp.service.RefreshTokenService;
import com.example.bankapp.service.UserService;
import com.example.bankapp.service.exception.auth.InvalidJwtTokenException;
import com.example.bankapp.service.exception.user.UserAuthenticationProcessingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final RefreshTokenService refreshTokenService;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public JwtResponseDTO authenticate(AuthenticationDTO authenticationDto) {
        try {
            log.info("Trying to authenticate user with email: {}", authenticationDto.getEmail());

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authenticationDto.getEmail(),
                            authenticationDto.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            UserResponseDTO user = userService.getUserByEmail(authenticationDto.getEmail());
            log.info("User authenticated successfully: {}", user.getName());

            String accessToken = jwtTokenProvider.generateAccessToken(userMapper.convertToUserFromResponseDTO(user));
            String refreshToken = jwtTokenProvider.generateRefreshToken(userMapper.convertToUserFromResponseDTO(user));
            String ip = getUserIp();

            RefreshToken newToken = buildRefreshToken(userMapper.convertToUserFromResponseDTO(user), refreshToken);

            refreshTokenRepository.findByUserAndIp(userMapper.convertToUserFromResponseDTO(user), ip).ifPresent(existingToken -> {
                log.info("Existing refresh token found for user {} and IP {}. Deleting it.", user.getName(), ip);
                refreshTokenService.deleteByUserAndIp(userMapper.convertToUserFromResponseDTO(user), ip);
            });

            refreshTokenService.save(newToken);
            log.info("New refresh token saved for user {} from IP {}", user.getName(), ip);

            return new JwtResponseDTO(accessToken, refreshToken);
        } catch (AuthenticationException exception) {
            log.error("Authentication failed for email {}: {}", authenticationDto.getEmail(), exception.getMessage());
            throw new UserAuthenticationProcessingException("Authentication error " + exception.getMessage());
        }
    }

    @Override
    @Transactional
    public JwtResponseDTO recreateToken(RefreshJwtRequestDTO refreshJwtRequestDto) {
        String requestToken = refreshJwtRequestDto.getRefreshToken();
        log.info("Attempting to recreate token using refresh token");

        if (jwtTokenProvider.validateRefreshToken(requestToken)) {
            String id = jwtTokenProvider.getLoginFromRefreshToken(requestToken);
            String ip = getUserIp();
            UserResponseDTO user = userService.getUserById(UUID.fromString(id));
            RefreshToken refreshToken = refreshTokenService.findByUserAndIp(userMapper.convertToUserFromResponseDTO(user), ip);
            String tokenValue = refreshToken.getToken();

            if (Objects.nonNull(tokenValue) && tokenValue.equals(requestToken)) {
                String accessToken = jwtTokenProvider.generateAccessToken(userMapper.convertToUserFromResponseDTO(user));
                String newRefreshToken = jwtTokenProvider.generateRefreshToken(userMapper.convertToUserFromResponseDTO(user));
                refreshTokenService.updateRefreshToken(refreshToken, newRefreshToken);
                log.info("Successfully recreated tokens for user {}", user.getName());
                return new JwtResponseDTO(accessToken, newRefreshToken);
            }
        }

        log.warn("Invalid or expired refresh token used for recreation");
        throw new InvalidJwtTokenException("JWT token is expired or invalid");
    }

    @Override
    @Transactional
    public void logout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (Objects.nonNull(authentication)) {
            String ip = getUserIp();
            String email = authentication.getName();
            log.info("Logging out user {}", email);
            UserResponseDTO user = userService.getUserByEmail(authentication.getName());
            refreshTokenService.deleteByUserAndIp(userMapper.convertToUserFromResponseDTO(user), ip);
            log.info("User {} logged out and refresh token deleted for IP {}", user.getName(), ip);
        } else {
            log.warn("Logout attempt with no authenticated user in context");
        }
    }

    private RefreshToken buildRefreshToken(User user, String refreshToken) {
        return RefreshToken.builder()
                .user(user)
                .token(refreshToken)
                .ipAddress(getUserIp())
                .expiryDate(jwtTokenProvider.getExpirationDate(refreshToken))
                .build();
    }

    private String getUserIp() {
        HttpServletRequest request =
                ((ServletRequestAttributes) RequestContextHolder
                        .currentRequestAttributes())
                        .getRequest();
        return request.getRemoteAddr();
    }
}
