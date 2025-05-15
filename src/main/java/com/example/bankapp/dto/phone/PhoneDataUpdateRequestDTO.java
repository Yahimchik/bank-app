package com.example.bankapp.dto.phone;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PhoneDataUpdateRequestDTO {
    @NotBlank(message = "New phone number must not be blank")
    @Size(min = 11, max = 11, message = "New phone must be exactly 11 digits")
    @Pattern(regexp = "^[0-9]{11}$", message = "New phone must contain only digits")
    private String oldPhone;

    @NotBlank(message = "New phone number must not be blank")
    @Size(min = 11, max = 11, message = "New phone must be exactly 11 digits")
    @Pattern(regexp = "^[0-9]{11}$", message = "New phone must contain only digits")
    private String newPhone;
}
