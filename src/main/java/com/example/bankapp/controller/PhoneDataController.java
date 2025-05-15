package com.example.bankapp.controller;

import com.example.bankapp.dto.phone.PhoneDataRequestDTO;
import com.example.bankapp.dto.phone.PhoneDataUpdateRequestDTO;
import com.example.bankapp.security.UserPrincipal;
import com.example.bankapp.service.PhoneDataService;
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
@RequestMapping("/api/v1/phone")
@RequiredArgsConstructor
@Validated
@SecurityRequirement(name = "Bearer Authentication")
public class PhoneDataController {

    private final PhoneDataService phoneDataService;

    @PostMapping
    @Operation(summary = "Add a new phone")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "Phone added"))
    public void addPhone(@Valid @RequestBody PhoneDataRequestDTO request, @AuthenticationPrincipal UserPrincipal user) {
        phoneDataService.addPhone(user.getId(), request.getPhone());
    }

    @DeleteMapping
    @Operation(summary = "Delete a phone")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "Phone deleted"))
    public void deletePhone(@Valid @RequestBody PhoneDataRequestDTO request, @AuthenticationPrincipal UserPrincipal user) {
        phoneDataService.deletePhone(user.getId(), request.getPhone());
    }

    @PutMapping
    @Operation(summary = "Update a phone")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "Phone updated"))
    public void updatePhone(@Valid @RequestBody PhoneDataUpdateRequestDTO request, @AuthenticationPrincipal UserPrincipal user) {
        phoneDataService.updatePhone(user.getId(), request.getOldPhone(), request.getNewPhone());
    }
}
