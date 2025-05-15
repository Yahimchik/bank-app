package com.example.bankapp.dto.user;

import com.example.bankapp.dto.account.AccountResponseDTO;
import com.example.bankapp.dto.email.EmailDataResponseDTO;
import com.example.bankapp.dto.phone.PhoneDataResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponseDTO {
        private UUID id;
        private String name;
        private LocalDate dateOfBirth;
        private List<EmailDataResponseDTO> emailData;
        private List<PhoneDataResponseDTO> phoneData;
        private AccountResponseDTO account;
}
