package com.example.bankapp.data;

import com.example.bankapp.entities.Account;

import java.math.BigDecimal;
import java.util.UUID;

public class AccountTestData {

    public static final UUID senderId = UUID.fromString("a61d45eb-7222-4e8d-b676-17649d8de189");
    public static final UUID receiverId = UUID.fromString("7a624fa1-a0ee-4cbf-9e05-195de02f6ccb");

    public static Account generateSenderAccount() {
        return Account.builder()
                .id(senderId)
                .balance(new BigDecimal("500.00"))
                .initialBalance(new BigDecimal("500.00"))
                .build();
    }

    public static Account generateReceiverAccount() {
        return Account.builder()
                .id(receiverId)
                .balance(new BigDecimal("200.00"))
                .initialBalance(new BigDecimal("200.00"))
                .build();
    }
}
