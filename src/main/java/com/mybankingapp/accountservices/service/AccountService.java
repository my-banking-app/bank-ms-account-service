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

/**
 * Service class for managing accounts.
 * Provides functionality for creating accounts, retrieving account details, and checking balances.
 */
@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    private final Random random = new SecureRandom();

    /**
     * Generates a unique 10-digit account number.
     * Ensures the generated account number does not already exist in the repository.
     *
     * @return A unique 10-digit account number as a String.
     */
    private String generateUniqueAccountNumber() {
        String number;
        do {
            number = String.format("%010d", random.nextInt(1_000_000_000));
        } while (accountRepository.existsByAccountNumber(number));
        return number;
    }

    /**
     * Creates a new account based on the provided request.
     *
     * @param request The account creation request containing account type, initial deposit, and customer ID.
     * @return The created Account object.
     */
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

    /**
     * Retrieves the balance of an active account by its ID.
     *
     * @param id The UUID of the account.
     * @return The balance of the account as a BigDecimal.
     * @throws RuntimeException if the account is not found or is inactive.
     */
    public BigDecimal getBalance(UUID id) {
        return accountRepository.findByIdAndActiveTrue(id)
                .map(Account::getBalance)
                .orElseThrow(() -> new RuntimeException("Account not found"));
    }

    /**
     * Retrieves an active account by its ID.
     *
     * @param id The UUID of the account.
     * @return The Account object.
     * @throws RuntimeException if the account is not found or is inactive.
     */
    public Account getAccount(UUID id) {
        return accountRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new RuntimeException("Account not found"));
    }
}
