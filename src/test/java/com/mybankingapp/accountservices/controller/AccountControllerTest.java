package com.mybankingapp.accountservices.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mybankingapp.accountservices.dto.AccountCreationRequest;
import com.mybankingapp.accountservices.model.Account;
import com.mybankingapp.accountservices.service.AccountService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AccountController.class)
@AutoConfigureMockMvc(addFilters = false)
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AccountService accountService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createAccount_deberiaRetornarCuentaYStatus201() throws Exception {
        AccountCreationRequest request = new AccountCreationRequest();
        request.setAccountType("savings");
        request.setInitialDeposit(BigDecimal.valueOf(1000));
        request.setCustomerId(UUID.randomUUID());

        Account cuentaCreada = Account.builder()
                .id(UUID.randomUUID())
                .accountNumber("1234567890")
                .accountType("savings")
                .balance(BigDecimal.valueOf(1000))
                .createdAt(LocalDateTime.now())
                .active(true)
                .customerId(request.getCustomerId())
                .build();

        Mockito.when(accountService.createAccount(Mockito.any(AccountCreationRequest.class)))
                .thenReturn(cuentaCreada);

        mockMvc.perform(post("/api/v1/accounts/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.accountNumber", is("1234567890")))
                .andExpect(jsonPath("$.accountType", is("savings")))
                .andExpect(jsonPath("$.balance", is(1000)));
    }

    @Test
    void getBalance_deberiaRetornarBalanceConStatus200() throws Exception {
        UUID accountId = UUID.randomUUID();
        BigDecimal balance = BigDecimal.valueOf(5000);

        Mockito.when(accountService.getBalance(accountId)).thenReturn(balance);

        mockMvc.perform(get("/api/v1/accounts/{id}/balance", accountId))
                .andExpect(status().isOk())
                .andExpect(content().string("5000"));
    }

    @Test
    void getAccount_deberiaRetornarCuentaConStatus200() throws Exception {
        UUID accountId = UUID.randomUUID();

        Account cuenta = Account.builder()
                .id(accountId)
                .accountNumber("9876543210")
                .accountType("checking")
                .balance(BigDecimal.valueOf(2500))
                .createdAt(LocalDateTime.now())
                .active(true)
                .customerId(UUID.randomUUID())
                .build();

        Mockito.when(accountService.getAccount(accountId)).thenReturn(cuenta);

        mockMvc.perform(get("/api/v1/accounts/{id}", accountId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(accountId.toString())))
                .andExpect(jsonPath("$.accountNumber", is("9876543210")))
                .andExpect(jsonPath("$.accountType", is("checking")))
                .andExpect(jsonPath("$.balance", is(2500)));
    }
}
