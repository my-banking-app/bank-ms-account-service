package com.mybankingapp.accountservices.dto;

    import jakarta.validation.constraints.DecimalMin;
    import jakarta.validation.constraints.NotNull;
    import lombok.Data;

    import java.math.BigDecimal;
    import java.util.UUID;

    /**
     * DTO (Data Transfer Object) for account creation requests.
     * Contains the necessary information to create a new account.
     */
    @Data
    public class AccountCreationRequest {

        /**
         * The type of the account to be created (e.g., savings, checking).
         * Must not be null.
         */
        @NotNull
        private String accountType;

        /**
         * The initial deposit amount for the account.
         * Must be greater than 0.0 and not null.
         */
        @NotNull
        @DecimalMin(value = "0.0", inclusive = false)
        private BigDecimal initialDeposit;

        /**
         * The unique identifier of the customer creating the account.
         * Must not be null.
         */
        @NotNull
        private UUID customerId;
    }