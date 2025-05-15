package com.example.bankapp.controller;

import com.example.bankapp.dto.auth.AuthenticationDTO;
import com.example.bankapp.dto.auth.JwtResponseDTO;
import com.example.bankapp.dto.auth.RefreshJwtRequestDTO;
import com.example.bankapp.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Validated
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    @Operation(summary = "Authenticate a user", description = "Authenticates a user and returns a JWT token.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully authenticated user")
    })
    public JwtResponseDTO authenticate(@Valid @RequestBody AuthenticationDTO authenticationDto) {
        return authenticationService.authenticate(authenticationDto);
    }

    @PostMapping("/token")
    @Operation(summary = "Recreate JWT token", description = "Recreates a JWT token using a refresh token.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully recreated token")
    })
    public JwtResponseDTO recreateToken(@Valid @RequestBody RefreshJwtRequestDTO refreshJwtRequestDto) {
        return authenticationService.recreateToken(refreshJwtRequestDto);
    }

    @PostMapping("/logout")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Logout the user", description = "Logs out the user and invalidates the session.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully logged out"),
            @ApiResponse(responseCode = "401", description = "Unauthorized, authentication required")
    })
    public void logout() {
        authenticationService.logout();
    }
}
