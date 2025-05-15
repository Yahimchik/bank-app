package com.example.bankapp.repo;

import com.example.bankapp.entities.EmailData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface EmailDataRepository extends JpaRepository<EmailData, UUID> {
    Optional<EmailData> findByEmail(String email);

    Optional<EmailData> findByUserId(UUID userId);

    boolean existsByEmail(String email);
}