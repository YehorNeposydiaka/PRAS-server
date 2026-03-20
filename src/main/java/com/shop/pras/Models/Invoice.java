package com.shop.pras.Models;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "invoices")
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private int invoiceNum;

    @Column(nullable = false)
    private String invoiceType; // INCOMING, WRITE_OFF, INVENTORY

    private String userName;
    private LocalDateTime invoiceTime;
    private String supplierName;
    private String paymentMethod;
    private String status; // "Збережена", "Проведена"
    private double totalSum;

    // Використовуємо ReceiptItem, зв'язок через поле invoice у ReceiptItem
    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReceiptItem> items = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "base_id", nullable = false)
    private Base base;

    public Invoice() {}

    // Допоміжний метод для синхронізації зв'язку
    public void addItem(ReceiptItem item) {
        items.add(item);
        item.setInvoice(this);
        recalculateTotal();
    }

    public void recalculateTotal() {
        if (items != null) {
            this.totalSum = items.stream()
                    .mapToDouble(item -> item.getQuantity() * item.getProduct().getCost())
                    .sum();
        }
    }

    // --- Getters and Setters ---
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public int getInvoiceNum() { return invoiceNum; }
    public void setInvoiceNum(int invoiceNum) { this.invoiceNum = invoiceNum; }

    public String getInvoiceType() { return invoiceType; }
    public void setInvoiceType(String invoiceType) { this.invoiceType = invoiceType; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public LocalDateTime getInvoiceTime() { return invoiceTime; }
    public void setInvoiceTime(LocalDateTime invoiceTime) { this.invoiceTime = invoiceTime; }

    public String getSupplierName() { return supplierName; }
    public void setSupplierName(String supplierName) { this.supplierName = supplierName; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public double getTotalSum() { return totalSum; }
    public void setTotalSum(double totalSum) { this.totalSum = totalSum; }

    public List<ReceiptItem> getItems() { return items; }
    public void setItems(List<ReceiptItem> items) {
        this.items = items;
        if (items != null) {
            items.forEach(item -> item.setInvoice(this));
        }
    }

    public Base getBase() { return base; }
    public void setBase(Base base) { this.base = base; }
}