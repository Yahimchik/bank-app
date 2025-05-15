package com.example.bankapp.controller;

import com.example.bankapp.dto.user.UserFilterDTO;
import com.example.bankapp.dto.user.UserResponseDTO;
import com.example.bankapp.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
@Slf4j
public class UserController {

    private final UserService userService;

    @GetMapping
    @Operation(summary = "Get a list of all users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of users")
    })
    public List<UserResponseDTO> getUsers(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) @DateTimeFormat(pattern = "dd.MM.yyyy") LocalDate dateOfBirth,
            @RequestParam(required = false) String emailData,
            @RequestParam(required = false) String phoneData,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        UserFilterDTO filter = new UserFilterDTO();
        filter.setName(name);
        filter.setDateOfBirth(dateOfBirth);
        filter.setEmailData(emailData);
        filter.setPhoneData(phoneData);

        PageRequest pageRequest = PageRequest.of(page, size);

        return userService.viewAllUsers(filter, pageRequest);
    }
}
