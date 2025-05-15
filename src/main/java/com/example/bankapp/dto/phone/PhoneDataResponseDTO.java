package com.example.bankapp.dto.phone;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PhoneDataResponseDTO {
    private UUID id;
    private String phone;
}
