package com.example.bankapp.service.impl;

import com.example.bankapp.entities.Account;
import com.example.bankapp.repo.AccountRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BalanceIncreaseService {

    private final AccountRepository accountRepository;

    private static final BigDecimal INCREASE_RATE = new BigDecimal("0.10"); // 10%
    private static final BigDecimal MAX_MULTIPLIER = new BigDecimal("2.07"); // 207%

    @Scheduled(fixedRate = 30000) // каждые 30 секунд
    @Transactional
    public void increaseBalances() {
        List<Account> accounts = accountRepository.findAll();

        for (Account account : accounts) {
            BigDecimal initial = account.getInitialBalance();
            BigDecimal maxBalance = initial.multiply(MAX_MULTIPLIER);
            BigDecimal current = account.getBalance();

            if (current.compareTo(maxBalance) < 0) {
                BigDecimal increased = current.multiply(INCREASE_RATE).setScale(2, RoundingMode.HALF_UP);
                BigDecimal newBalance = current.add(increased);

                if (newBalance.compareTo(maxBalance) > 0) {
                    newBalance = maxBalance;
                }

                account.setBalance(newBalance);
                log.info("Updated balance for account {} to {}", account.getId(), newBalance);
            }
        }

        accountRepository.saveAll(accounts);
    }
}
