package com.example.bankapp.service.impl;

import com.example.bankapp.entities.PhoneData;
import com.example.bankapp.entities.User;
import com.example.bankapp.repo.PhoneDataRepository;
import com.example.bankapp.repo.UserRepository;
import com.example.bankapp.service.PhoneDataService;
import com.example.bankapp.service.exception.phone.NoAccessToPhoneException;
import com.example.bankapp.service.exception.phone.PhoneAlreadyInUseException;
import com.example.bankapp.service.exception.phone.PhoneNotFoundException;
import com.example.bankapp.service.exception.user.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PhoneDataServiceImpl implements PhoneDataService {

    private final PhoneDataRepository phoneDataRepository;
    private final UserRepository userRepository;

    @Override
    @Caching(evict = {
            @CacheEvict(value = "userCache", allEntries = true),
            @CacheEvict(value = "userByEmailCache", allEntries = true)
    })
    @Transactional
    public void addPhone(UUID userId, String phone) {
        if (phoneDataRepository.existsByPhone(phone)) {
            throw new PhoneAlreadyInUseException("Phone is already used");
        }
        User user = getUser(userId);

        PhoneData phoneData = PhoneData.builder()
                .phone(phone)
                .user(user)
                .build();

        phoneDataRepository.save(phoneData);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "userCache", allEntries = true),
            @CacheEvict(value = "userByEmailCache", allEntries = true)
    })
    @Transactional
    public void deletePhone(UUID userId, String phone) {
        PhoneData phoneData = getOwnedPhone(userId, phone);
        User user = phoneData.getUser();
        user.getPhoneData().remove(phoneData);
        phoneDataRepository.delete(phoneData);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "userCache", allEntries = true),
            @CacheEvict(value = "userByEmailCache", allEntries = true)
    })
    @Transactional
    public void updatePhone(UUID userId, String oldPhone, String newPhone) {
        if (phoneDataRepository.existsByPhone(newPhone)) {
            throw new PhoneAlreadyInUseException("New phone is already in use");
        }

        PhoneData phoneData = getOwnedPhone(userId, oldPhone);
        phoneData.setPhone(newPhone);
        phoneDataRepository.save(phoneData);
    }

    public User getUser(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    public PhoneData getOwnedPhone(UUID userId, String phone) {
        PhoneData phoneData = phoneDataRepository.findByPhone(phone)
                .orElseThrow(() -> new PhoneNotFoundException("Phone not found"));

        if (!phoneData.getUser().getId().equals(userId)) {
            throw new NoAccessToPhoneException("You can't modify another user's phone");
        }

        return phoneData;
    }
}
