package com.example.bankapp.service;

import com.example.bankapp.data.AccountTestData;
import com.example.bankapp.entities.Account;
import com.example.bankapp.repo.AccountRepository;
import com.example.bankapp.service.exception.account.AccountNotFoundException;
import com.example.bankapp.service.exception.transaction.InsufficientFundsException;
import com.example.bankapp.service.exception.transaction.InvalidAmountException;
import com.example.bankapp.service.exception.transaction.TransferToSameUserException;
import com.example.bankapp.service.impl.TransferServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class TransferServiceTest {
    private static final UUID senderId = UUID.fromString("a61d45eb-7222-4e8d-b676-17649d8de189");
    private static final UUID receiverId = UUID.fromString("7a624fa1-a0ee-4cbf-9e05-195de02f6ccb");
    private static final BigDecimal amount = new BigDecimal("100.00");

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private TransferServiceImpl transferService;

    private Account senderAccount;
    private Account receiverAccount;

    @BeforeEach
    void setUpAccounts() {
        createTestData();
    }

    @Test
    void testSuccessfulTransfer() {
        when(accountRepository.findByUserId(senderId)).thenReturn(Optional.of(senderAccount));
        when(accountRepository.findByUserId(receiverId)).thenReturn(Optional.of(receiverAccount));

        transferService.transferMoney(senderId, receiverId, amount);

        assertEquals(new BigDecimal("400.00"), senderAccount.getBalance());
        assertEquals(new BigDecimal("300.00"), receiverAccount.getBalance());

        ArgumentCaptor<List<Account>> captor = ArgumentCaptor.forClass(List.class);
        verify(accountRepository).saveAll(captor.capture());

        List<Account> savedAccounts = captor.getValue();
        assertTrue(savedAccounts.contains(senderAccount));
        assertTrue(savedAccounts.contains(receiverAccount));
    }

    @Test
    void testTransferWithInsufficientFunds() {
        senderAccount.setBalance(new BigDecimal("50.00"));

        when(accountRepository.findByUserId(senderId)).thenReturn(Optional.of(senderAccount));
        when(accountRepository.findByUserId(receiverId)).thenReturn(Optional.of(receiverAccount));

        assertThrows(InsufficientFundsException.class, () ->
                transferService.transferMoney(senderId, receiverId, amount)
        );

        verify(accountRepository, never()).saveAll(any());
    }

    @Test
    void testTransferToSameUser() {
        assertThrows(TransferToSameUserException.class, () ->
                transferService.transferMoney(senderId, senderId, amount)
        );

        verify(accountRepository, never()).findByUserId(any());
        verify(accountRepository, never()).saveAll(any());
    }

    @Test
    void testTransferWithInvalidAmount_Null() {
        assertThrows(InvalidAmountException.class, () ->
                transferService.transferMoney(senderId, receiverId, null)
        );

        verify(accountRepository, never()).findByUserId(any());
        verify(accountRepository, never()).saveAll(any());
    }

    @Test
    void testTransferWithInvalidAmount_Zero() {
        assertThrows(InvalidAmountException.class, () ->
                transferService.transferMoney(senderId, receiverId, BigDecimal.ZERO)
        );

        verify(accountRepository, never()).findByUserId(any());
        verify(accountRepository, never()).saveAll(any());
    }

    @Test
    void testSenderAccountNotFound() {
        when(accountRepository.findByUserId(senderId)).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, () ->
                transferService.transferMoney(senderId, receiverId, amount)
        );

        verify(accountRepository, never()).saveAll(any());
    }

    @Test
    void testReceiverAccountNotFound() {
        when(accountRepository.findByUserId(senderId)).thenReturn(Optional.of(senderAccount));
        when(accountRepository.findByUserId(receiverId)).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, () ->
                transferService.transferMoney(senderId, receiverId, amount)
        );

        verify(accountRepository, never()).saveAll(any());
    }

    private void createTestData() {
        senderAccount = AccountTestData.generateSenderAccount();
        receiverAccount = AccountTestData.generateReceiverAccount();
    }
}
