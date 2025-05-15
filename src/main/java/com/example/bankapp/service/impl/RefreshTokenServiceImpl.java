package com.example.bankapp.service.impl;

import com.example.bankapp.entities.RefreshToken;
import com.example.bankapp.entities.User;
import com.example.bankapp.repo.RefreshTokenRepository;
import com.example.bankapp.security.jwt.JwtTokenProvider;
import com.example.bankapp.service.RefreshTokenService;
import com.example.bankapp.service.exception.auth.TokenNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    @Transactional
    public RefreshToken save(RefreshToken token) {
        log.info("Saving refresh token for user: {} with IP: {}", token.getUser().getName(), token.getIpAddress());
        return refreshTokenRepository.save(token);
    }

    @Override
    @Cacheable(value = "refreshTokenCache", key = "'user:' + #user.id + ':ip:' + #ip")
    public RefreshToken findByUserAndIp(User user, String ip) {
        log.info("Searching for refresh token for user: {} with IP: {}", user.getName(), ip);
        return refreshTokenRepository.findByUserAndIp(user, ip)
                .orElseThrow(() -> {
                    log.warn("Refresh token not found for user: {} with IP: {}", user.getName(), ip);
                    return new TokenNotFoundException("Refresh token for user " + user.getName() + " not found");
                });
    }

    @Override
    @Transactional
    @CacheEvict(value = "refreshTokenCache", key = "'user:' + #token.user.id + ':ip:' + #token.ipAddress")
    public RefreshToken updateRefreshToken(RefreshToken token, String newToken) {
        log.info("Updating refresh token for user: {} with IP: {}", token.getUser().getName(), token.getIpAddress());
        token.setToken(newToken);
        token.setExpiryDate(jwtTokenProvider.getExpirationDate(newToken));
        return refreshTokenRepository.save(token);
    }

    @Override
    @Transactional
    @CacheEvict(value = "refreshTokenCache", key = "'user:' + #user.id + ':ip:' + #ip")
    public void deleteByUserAndIp(User user, String ip) {
        log.info("Deleting refresh token for user: {} with IP: {}", user.getName(), ip);
        refreshTokenRepository.deleteByUserAndIp(user, ip);
    }
}
