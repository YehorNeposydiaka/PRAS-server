package com.shop.pras.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "receipt_items")
public class ReceiptItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "receipt_id", nullable = true) // Може бути null, якщо це накладна
    private Receipt receipt;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "invoice_id", nullable = true) // Може бути null, якщо це чек
    private Invoice invoice;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    private double quantity;

    public ReceiptItem() {}

    public ReceiptItem(Product product, double quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    // --- Getters and Setters ---
    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }

    public double getQuantity() { return quantity; }
    public void setQuantity(double quantity) { this.quantity = quantity; }

    public Receipt getReceipt() { return receipt; }
    public void setReceipt(Receipt receipt) { this.receipt = receipt; }

    public Invoice getInvoice() { return invoice; }
    public void setInvoice(Invoice invoice) { this.invoice = invoice; }
}