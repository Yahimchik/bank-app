package com.example.bankapp.security;

import com.example.bankapp.entities.EmailData;
import com.example.bankapp.entities.User;
import com.example.bankapp.repo.EmailDataRepository;
import com.example.bankapp.repo.UserRepository;
import com.example.bankapp.service.exception.email.EmailNotFoundException;
import com.example.bankapp.service.exception.user.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final EmailDataRepository emailDataRepository;
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        EmailData emailData = emailDataRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User with email " + email + " not found"));

        User user = emailData.getUser();

        return new UserPrincipal(
                user.getId(),
                emailData.getEmail(),
                user.getPassword()
        );
    }

    public UserDetails loadUserById(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + userId + " not found"));

        return new UserPrincipal(
                user.getId(),
                user.getEmailData().get(0).getEmail(),
                user.getPassword()
        );
    }
}
