package com.mybankingapp.accountservices.controller;

import com.mybankingapp.accountservices.dto.AccountCreationRequest;
import com.mybankingapp.accountservices.model.Account;
import com.mybankingapp.accountservices.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * REST controller for managing account-related operations.
 * Provides endpoints for account registration, balance retrieval, and account details retrieval.
 */
@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
@Slf4j
public class AccountController {

    private final AccountService accountService;

    /**
     * Endpoint to register a new account.
     *
     * @param request The request object containing the necessary details to create an account.
     *                Must be valid according to validation annotations.
     * @return An HTTP response with status CREATED and the created account object.
     */
    @PostMapping("/register")
    public ResponseEntity<Account> createAccount(@Valid @RequestBody AccountCreationRequest request) {
        log.info(">>> Solicitud de creación de cuenta recibida");
        Account created = accountService.createAccount(request);
        log.info("<<< Cuenta creada: {}", created.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * Endpoint to retrieve the balance of an active account.
     *
     * @param id The UUID of the account whose balance is to be retrieved.
     * @return An HTTP response with the account balance as a BigDecimal.
     */
    @GetMapping("/{id}/balance")
    public ResponseEntity<BigDecimal> getBalance(@PathVariable UUID id) {
        log.info(">>> Consultando balance para cuenta {}", id);
        BigDecimal balance = accountService.getBalance(id);
        log.info("<<< Balance obtenido para cuenta {}", id);
        return ResponseEntity.ok(balance);
    }

    /**
     * Endpoint to retrieve the details of an active account.
     *
     * @param id The UUID of the account to be retrieved.
     * @return An HTTP response with the account object.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Account> getAccount(@PathVariable UUID id) {
        log.info(">>> Consultando datos de la cuenta {}", id);
        Account account = accountService.getAccount(id);
        log.info("<<< Cuenta {} recuperada", id);
        return ResponseEntity.ok(account);
    }
}
