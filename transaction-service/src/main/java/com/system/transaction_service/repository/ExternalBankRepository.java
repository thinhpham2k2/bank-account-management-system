package com.system.transaction_service.repository;

import com.system.transaction_service.entity.ExternalBank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExternalBankRepository extends JpaRepository<ExternalBank, String> {
}
