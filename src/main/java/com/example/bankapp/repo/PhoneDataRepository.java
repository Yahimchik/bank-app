package com.example.bankapp.repo;

import com.example.bankapp.entities.PhoneData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PhoneDataRepository extends JpaRepository<PhoneData, UUID> {
    Optional<PhoneData> findByPhone(String phone);

    boolean existsByPhone(String phone);
}