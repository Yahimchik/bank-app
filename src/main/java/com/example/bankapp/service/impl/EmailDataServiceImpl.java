package com.example.bankapp.service.impl;

import com.example.bankapp.entities.EmailData;
import com.example.bankapp.entities.User;
import com.example.bankapp.repo.EmailDataRepository;
import com.example.bankapp.repo.UserRepository;
import com.example.bankapp.service.EmailDataService;
import com.example.bankapp.service.exception.email.EmailAlreadyInUseException;
import com.example.bankapp.service.exception.email.NoAccessToEmailException;
import com.example.bankapp.service.exception.phone.PhoneNotFoundException;
import com.example.bankapp.service.exception.user.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailDataServiceImpl implements EmailDataService {

    private final EmailDataRepository emailDataRepository;
    private final UserRepository userRepository;

    @Override
    @Caching(evict = {
            @CacheEvict(value = "userCache", allEntries = true),
            @CacheEvict(value = "userByEmailCache", key = "#email")

    })
    @Transactional
    public void addEmail(UUID userId, String email) {
        isEmailExist(email, "Email is already used");
        User user = getUser(userId);

        EmailData emailData = EmailData.builder()
                .email(email)
                .user(user)
                .build();

        emailDataRepository.save(emailData);
        log.info("Added email: {}", email);
    }

    @Transactional
    @Override
    @Caching(evict = {
            @CacheEvict(value = "userCache", allEntries = true),
            @CacheEvict(value = "userByEmailCache", key = "#email")
    })
    public void deleteEmail(UUID userId, String email) {
        EmailData emailData = getOwnedEmail(userId, email);
        log.info("Deleting email : {}", emailData.getEmail());

        User user = emailData.getUser();
        user.getEmailData().remove(emailData);  // Убираем из коллекции User
        emailDataRepository.delete(emailData);
        log.info("Deleted email: {}", emailData.getEmail());
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "userByEmailCache", key = "#oldEmail"),
            @CacheEvict(value = "userByEmailCache", key = "#newEmail"),
            @CacheEvict(value = "userCache", allEntries = true)
    })
    @Transactional
    public void updateEmail(UUID userId, String oldEmail, String newEmail) {
        isEmailExist(newEmail, "New email is already in use");

        EmailData emailData = getOwnedEmail(userId, oldEmail);
        emailData.setEmail(newEmail);
        emailDataRepository.save(emailData);
        log.info("Updated email: {}", newEmail);
    }

    private void isEmailExist(String newEmail, String message) {
        if (emailDataRepository.existsByEmail(newEmail)) {
            log.error(message);
            throw new EmailAlreadyInUseException(message);
        }
    }

    private User getUser(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    private EmailData getOwnedEmail(UUID userId, String email) {
        EmailData emailData = emailDataRepository.findByEmail(email)
                .orElseThrow(() -> new PhoneNotFoundException("Email not found"));

        if (!emailData.getUser().getId().equals(userId)) {
            throw new NoAccessToEmailException("You can't modify another user's email");
        }

        return emailData;
    }
}
