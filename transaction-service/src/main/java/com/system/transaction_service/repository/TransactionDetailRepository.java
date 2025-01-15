package com.system.transaction_service.repository;

import com.system.common_library.enums.*;
import com.system.transaction_service.entity.TransactionDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionDetailRepository extends JpaRepository<TransactionDetail, String> {

    Optional<TransactionDetail> findByIdAndStatus(String id, Boolean status);

    @Query("SELECT d FROM TransactionDetail d " +
            "WHERE d.status = ?1 " +
            "AND (:#{#directionList.size()} = 0 OR d.direction IN ?2) " +
            "AND (:#{#feePayerList.size()} = 0 OR d.transaction.feePayer IN ?3) " +
            "AND (:#{#initiatorList.size()} = 0 OR d.transaction.initiator IN ?4) " +
            "AND (:#{#methodList.size()} = 0 OR d.transaction.method IN ?5) " +
            "AND (:#{#transactionTypeList.size()} = 0 OR d.transaction.transactionType IN ?6) " +
            "AND (d.transaction.senderAccount LIKE %?7% " +
            "OR d.transaction.senderAccountName LIKE %?7% " +
            "OR d.transaction.receiverAccount LIKE %?7% " +
            "OR d.transaction.receiverAccountName LIKE %?7% " +
            "OR d.transaction.note LIKE %?7% " +
            "OR d.transaction.description LIKE %?7%)" +
            "AND d.transaction.amount BETWEEN ?8 AND ?9")
    Page<TransactionDetail> findAllByCondition(
            Boolean status, List<Direction> directionList, List<FeePayer> feePayerList, List<Initiator> initiatorList,
            List<Method> methodList, List<TransactionType> transactionTypeList, String search, BigDecimal amountStart,
            BigDecimal amountEnd, Pageable pageable);
}
