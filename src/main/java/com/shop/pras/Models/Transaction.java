package com.shop.pras.Models;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String cashBoxName;

    @Column(nullable = false)
    private String userName;

    @Column(nullable = false)
    private double balanceBefore;

    @Column(nullable = false)
    private double operationSum;

    @Column(nullable = false)
    private double balanceAfter;

    @Column(nullable = false)
    private LocalDateTime operationTime; // Виправлено назву з opetationTime

    @Column(nullable = false)
    private String operationType;

    @Column(nullable = false)
    private String documentNum;

    @ManyToOne
    @JoinColumn(
            name = "base_login",
            referencedColumnName = "baseLogin",
            nullable = false
    )
    private Base base;

    // Constructors
    public Transaction() {}

    // Getters and Setters
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getCashBoxName() { return cashBoxName; }
    public void setCashBoxName(String cashBoxName) { this.cashBoxName = cashBoxName; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public double getBalanceBefore() { return balanceBefore; }
    public void setBalanceBefore(double balanceBefore) { this.balanceBefore = balanceBefore; }

    public double getOperationSum() { return operationSum; }
    public void setOperationSum(double operationSum) { this.operationSum = operationSum; }

    public double getBalanceAfter() { return balanceAfter; }
    public void setBalanceAfter(double balanceAfter) { this.balanceAfter = balanceAfter; }

    public LocalDateTime getOperationTime() { return operationTime; }
    public void setOperationTime(LocalDateTime operationTime) { this.operationTime = operationTime; }

    public String getOperationType() { return operationType; }
    public void setOperationType(String operationType) { this.operationType = operationType; }

    public String getDocumentNum() { return documentNum; }
    public void setDocumentNum(String documentNum) { this.documentNum = documentNum; }

    public Base getBase() { return base; }
    public void setBase(Base base) { this.base = base; }
}