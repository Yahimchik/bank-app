package com.example.bankapp.service.impl;

import com.example.bankapp.entities.Account;
import com.example.bankapp.repo.AccountRepository;
import com.example.bankapp.service.TransferService;
import com.example.bankapp.service.exception.account.AccountNotFoundException;
import com.example.bankapp.service.exception.transaction.InsufficientFundsException;
import com.example.bankapp.service.exception.transaction.InvalidAmountException;
import com.example.bankapp.service.exception.transaction.TransferToSameUserException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransferServiceImpl implements TransferService {
    private final AccountRepository accountRepository;

    @Override
    @Transactional
    public void transferMoney(UUID fromUserId, UUID toUserId, BigDecimal amount) {
        validateTransfer(fromUserId, toUserId, amount);

        Account sender = findUserAccount(fromUserId, "Sender account not found");
        Account receiver = findUserAccount(toUserId, "Receiver account not found");

        if (sender.getBalance().compareTo(amount) < 0) {
            log.error("Sender account balance less than amount");
            throw new InsufficientFundsException("Insufficient funds");
        }

        sender.setBalance(sender.getBalance().subtract(amount));
        receiver.setBalance(receiver.getBalance().add(amount));

        accountRepository.saveAll(List.of(sender, receiver));

        log.info("Transferred {} from {} to {}", amount, fromUserId, toUserId);
    }

    private Account findUserAccount(UUID userId, String message) {
        return accountRepository.findByUserId(userId)
                .orElseThrow(() -> {
                    log.error("User {} not found", userId);
                    return new AccountNotFoundException(message);
                });
    }

    private void validateTransfer(UUID fromUserId, UUID toUserId, BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            log.error("Invalid amount {}", amount);
            throw new InvalidAmountException("Transfer amount must be positive");
        }

        if (fromUserId.equals(toUserId)) {
            log.error("Invalid transfer to same user id {}", fromUserId);
            throw new TransferToSameUserException("Cannot transfer money to yourself");
        }
    }
}
