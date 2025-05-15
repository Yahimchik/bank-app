package com.example.bankapp.dto.auth;

import com.example.bankapp.validation.password.ValidPassword;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthenticationDTO {
    @NotBlank(message = "Email must not be blank")
    @Email(message = "Email must be valid")
    @Schema(description = "User email", example = "user1@example.com")
    private String email;

    @NotBlank(message = "Password must not be blank")
    @ValidPassword
    @Schema(description = "User password", example = "password1")
    private String password;
}
