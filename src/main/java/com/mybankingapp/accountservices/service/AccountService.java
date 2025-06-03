package com.mybankingapp.accountservices.service;

import com.mybankingapp.accountservices.dto.AccountCreationRequest;
import com.mybankingapp.accountservices.model.Account;
import com.mybankingapp.accountservices.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    private final Random random = new SecureRandom();

    private String generateUniqueAccountNumber() {
        String number;
        do {
            number = String.format("%010d", random.nextInt(1_000_000_000));
        } while (accountRepository.existsByAccountNumber(number));
        return number;
    }


    public Account createAccount(AccountCreationRequest request) {
        String accountNumber = generateUniqueAccountNumber();

        Account account = Account.builder()
                .accountNumber(accountNumber)
                .accountType(request.getAccountType())
                .balance(request.getInitialDeposit())
                .customerId(request.getCustomerId())
                .createdAt(LocalDateTime.now())
                .active(true)
                .build();

        return accountRepository.save(account);
    }

    public BigDecimal getBalance(UUID id) {
        return accountRepository.findByIdAndActiveTrue(id)
                .map(Account::getBalance)
                .orElseThrow(() -> new RuntimeException("Account not found"));
    }

    public Account getAccount(UUID id) {
        return accountRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new RuntimeException("Account not found"));
    }
}

