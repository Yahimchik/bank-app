package com.example.bankapp.service;

import java.math.BigDecimal;
import java.util.UUID;

public interface TransferService {
    void transferMoney(UUID fromUserId, UUID toUserId, BigDecimal amount);
}
