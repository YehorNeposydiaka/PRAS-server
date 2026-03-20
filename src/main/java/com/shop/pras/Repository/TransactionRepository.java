package com.shop.pras.Repository;

import com.shop.pras.Models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query("SELECT t FROM Transaction t WHERE t.base.baseLogin = :baseLogin " +
            "AND t.operationTime BETWEEN :start AND :end " +
            "AND (:cashier IS NULL OR t.userName = :cashier) " +
            "AND (:payment IS NULL OR t.cashBoxName = :payment) " +
            "AND (:opType IS NULL OR t.operationType = :opType)")
    List<Transaction> findByBaseAndDateRange(
            @Param("baseLogin") String baseLogin,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("cashier") String cashier,
            @Param("payment") String payment,
            @Param("opType") String opType
    );
}