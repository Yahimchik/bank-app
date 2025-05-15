package com.example.bankapp.controller;

import com.example.bankapp.dto.email.EmailDataRequestDTO;
import com.example.bankapp.dto.email.EmailDataUpdateRequestDTO;
import com.example.bankapp.security.UserPrincipal;
import com.example.bankapp.service.EmailDataService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/email")
@RequiredArgsConstructor
@Validated
@SecurityRequirement(name = "Bearer Authentication")
public class EmailDataController {

    private final EmailDataService emailDataService;

    @PostMapping
    @Operation(summary = "Add a new email")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "Email added"))
    public void addEmail(@Valid @RequestBody EmailDataRequestDTO request, @AuthenticationPrincipal UserPrincipal user) {
        emailDataService.addEmail(user.getId(), request.getEmail());
    }

    @DeleteMapping
    @Operation(summary = "Delete an existing email")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "Email deleted"))
    public void deleteEmail(@Valid @RequestBody EmailDataRequestDTO request, @AuthenticationPrincipal UserPrincipal user) {
        emailDataService.deleteEmail(user.getId(), request.getEmail());
    }

    @PutMapping
    @Operation(summary = "Update an email")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "Email updated"))
    public void updateEmail(@Valid @RequestBody EmailDataUpdateRequestDTO request, @AuthenticationPrincipal UserPrincipal user) {
        emailDataService.updateEmail(user.getId(), request.getOldEmail(), request.getNewEmail());
    }
}
