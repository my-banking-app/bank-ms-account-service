package com.mybankingapp.accountservices.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "account")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(unique = true, length = 10)
    private String accountNumber;

    private String accountType;
    private BigDecimal balance;
    private LocalDateTime createdAt;
    private boolean active;

    @Column(unique = true, nullable = false)
    private UUID customerId;

}

