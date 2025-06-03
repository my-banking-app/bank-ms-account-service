package com.mybankingapp.accountservices.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class AccountCreationRequest {
    @NotNull
    private String accountType;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal initialDeposit;

    @NotNull
    private UUID customerId;
}

