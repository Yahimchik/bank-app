package com.example.bankapp.controller;

import com.example.bankapp.security.UserPrincipal;
import com.example.bankapp.service.TransferService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequestMapping("/api/transfer")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
public class TransferController {

    private final TransferService transferService;

    @PostMapping
    @Operation(summary = "Transfer money from user1 to user2")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "Transfer successful"))
    public ResponseEntity<?> transferMoney(
            @AuthenticationPrincipal UserPrincipal user,
            @RequestParam("toUserId") UUID toUserId,
            @RequestParam("amount") BigDecimal amount) {

        transferService.transferMoney(user.getId(), toUserId, amount);
        return ResponseEntity.ok("Transfer successful");
    }
}
