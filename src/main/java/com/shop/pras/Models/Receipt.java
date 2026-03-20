package com.shop.pras.Models;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "checks")
public class Receipt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private int receiptNum;

    private String shopName;
    private String shopAddress;
    private String cashierName;
    private LocalDateTime receiptTime;

    // Тепер правильно: тип ReceiptItem
    @OneToMany(mappedBy = "receipt", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReceiptItem> items = new ArrayList<>();

    private double totalSum;
    private String paymentMethod;

    @Column(nullable = false)
    private boolean isReturn;

    @ManyToOne
    @JoinColumn(name = "base_id", nullable = false)
    private Base base;

    public Receipt() {}

    // Оновлений конструктор: приймає список ReceiptItem
    public Receipt(int receiptNum, String shopName, String shopAddress, String cashierName,
                   LocalDateTime receiptTime, List<ReceiptItem> items, double totalSum, String paymentMethod, boolean isReturn) {
        this.receiptNum = receiptNum;
        this.shopName = shopName;
        this.shopAddress = shopAddress;
        this.cashierName = cashierName;
        this.receiptTime = receiptTime;
        this.items = items;
        this.totalSum = totalSum;
        this.paymentMethod = paymentMethod;
        this.isReturn = isReturn;
    }

    // Методи керування списком
    public void addItem(ReceiptItem item) {
        items.add(item);
        item.setReceipt(this);
    }

    // --- GETTERS AND SETTERS ---

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public int getReceiptNum() { return receiptNum; }
    public void setReceiptNum(int receiptNum) { this.receiptNum = receiptNum; }

    public String getShopName() { return shopName; }
    public void setShopName(String shopName) { this.shopName = shopName; }

    public String getShopAddress() { return shopAddress; }
    public void setShopAddress(String shopAddress) { this.shopAddress = shopAddress; }

    public String getCashierName() { return cashierName; }
    public void setCashierName(String cashierName) { this.cashierName = cashierName; }

    public LocalDateTime getReceiptTime() { return receiptTime; }
    public void setReceiptTime(LocalDateTime receiptTime) { this.receiptTime = receiptTime; }

    public List<ReceiptItem> getItems() { return items; }
    public void setItems(List<ReceiptItem> items) { this.items = items; }

    public double getTotalSum() { return totalSum; }
    public void setTotalSum(double totalSum) { this.totalSum = totalSum; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public boolean getIsReturn() {return isReturn;}
    public void setIsReturn(boolean isReturn) {this.isReturn = isReturn;}

    public Base getBase() { return base; }
    public void setBase(Base base) { this.base = base; }
}