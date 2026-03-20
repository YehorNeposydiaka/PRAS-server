package com.shop.pras.Repository;

import com.shop.pras.Models.Receipt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReceiptRepository extends JpaRepository<Receipt, Integer> {

    @Query("SELECT DISTINCT r FROM Receipt r LEFT JOIN FETCH r.items " +
            "WHERE r.base.baseLogin = :baseLogin " +
            "AND r.receiptTime BETWEEN :start AND :end " +
            "AND (:cashier IS NULL OR r.cashierName = :cashier) " +
            "AND (:payment IS NULL OR r.paymentMethod = :payment) " +
            "AND (:isReturn IS NULL OR r.isReturn = :isReturn)")
    List<Receipt> findComplex(
            @Param("baseLogin") String baseLogin,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("cashier") String cashier,
            @Param("payment") String payment,
            @Param("isReturn") Boolean isReturn
    );
}