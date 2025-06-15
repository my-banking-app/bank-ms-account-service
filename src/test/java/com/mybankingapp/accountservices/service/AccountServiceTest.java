package com.mybankingapp.accountservices.service;

import com.mybankingapp.accountservices.dto.AccountCreationRequest;
import com.mybankingapp.accountservices.model.Account;
import com.mybankingapp.accountservices.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class AccountServiceTest {

    private AccountRepository accountRepository;
    private AccountService accountService;

    @BeforeEach
    void setUp() {
        accountRepository = mock(AccountRepository.class);
        accountService = new AccountService(accountRepository);
    }

    @Test
    void createAccount_deberiaGuardarCuentaConDatosEsperados() {
        AccountCreationRequest request = new AccountCreationRequest();
        request.setAccountType("savings");
        request.setInitialDeposit(BigDecimal.valueOf(1000));
        UUID customerId = UUID.randomUUID();
        request.setCustomerId(customerId);

        // Simular que no existe ese número de cuenta
        when(accountRepository.existsByAccountNumber(anyString())).thenReturn(false);

        // Simular guardado
        when(accountRepository.save(any(Account.class)))
                .thenAnswer(invocation -> {
                    Account acc = invocation.getArgument(0);
                    acc.setId(UUID.randomUUID());
                    return acc;
                });

        Account created = accountService.createAccount(request);

        assertThat(created.getId()).isNotNull();
        assertThat(created.getAccountNumber()).hasSize(10);
        assertThat(created.getAccountType()).isEqualTo("savings");
        assertThat(created.getBalance()).isEqualTo(BigDecimal.valueOf(1000));
        assertThat(created.getCustomerId()).isEqualTo(customerId);
        assertThat(created.isActive()).isTrue();

        verify(accountRepository, times(1)).save(any(Account.class));
    }

    @Test
    void getBalance_deberiaRetornarBalanceCuandoCuentaExisteYEsActiva() {
        UUID accountId = UUID.randomUUID();
        BigDecimal balanceEsperado = BigDecimal.valueOf(5000);
        Account cuenta = Account.builder()
                .id(accountId)
                .balance(balanceEsperado)
                .active(true)
                .build();

        when(accountRepository.findByIdAndActiveTrue(accountId)).thenReturn(Optional.of(cuenta));

        BigDecimal balance = accountService.getBalance(accountId);

        assertThat(balance).isEqualTo(balanceEsperado);
    }

    @Test
    void getBalance_deberiaLanzarExcepcionSiCuentaNoExiste() {
        UUID accountId = UUID.randomUUID();
        when(accountRepository.findByIdAndActiveTrue(accountId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> accountService.getBalance(accountId))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Account not found");
    }

    @Test
    void getAccount_deberiaRetornarCuentaSiExisteYEsActiva() {
        UUID accountId = UUID.randomUUID();
        Account cuenta = Account.builder()
                .id(accountId)
                .active(true)
                .build();

        when(accountRepository.findByIdAndActiveTrue(accountId)).thenReturn(Optional.of(cuenta));

        Account result = accountService.getAccount(accountId);

        assertThat(result).isEqualTo(cuenta);
    }

    @Test
    void getAccount_deberiaLanzarExcepcionSiCuentaNoExiste() {
        UUID accountId = UUID.randomUUID();
        when(accountRepository.findByIdAndActiveTrue(accountId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> accountService.getAccount(accountId))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Account not found");
    }
}
