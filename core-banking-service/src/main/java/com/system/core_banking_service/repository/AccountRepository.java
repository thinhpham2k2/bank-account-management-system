package com.system.core_banking_service.repository;

import com.system.core_banking_service.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {

    Boolean existsByAccountId(String code);

    Boolean existsByAccountNumber(String code);

    Optional<Account> findByAccountNumberAndStatus(String accountNumber, Boolean status);
}
