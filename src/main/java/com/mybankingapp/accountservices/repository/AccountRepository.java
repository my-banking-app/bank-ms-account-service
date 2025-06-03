package com.mybankingapp.accountservices.repository;

import com.mybankingapp.accountservices.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AccountRepository extends JpaRepository<Account, UUID> {
    Optional<Account> findByIdAndActiveTrue(UUID id);
    boolean existsByAccountNumber(String accountNumber);
}
