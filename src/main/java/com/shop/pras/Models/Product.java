package com.shop.pras.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int productId;

    @Column(nullable = false)
    private String code;

    @Column(nullable = false)
    private String productName;

    private double cost;

    private double price;

    private double quantity;

    @Column(name = "is_deleted")
    private boolean isDeleted = false;

    // Связь с базой
    @ManyToOne
    @JoinColumn(name = "base_id", nullable = false)

    private Base base;

    public Product() {}

    public Product(String code, String name, double cost, double price, double quantity){
        this.code = code;
        this.productName = name;
        this.cost = cost;
        this.price = price;
        this.quantity = quantity;
    }

    // Геттери и сеттери
    public int getProductId(){return productId;}

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public double getCost() { return cost; }
    public void setCost(double cost) { this.cost = cost; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public double getQuantity() { return quantity; }
    public void setQuantity(double quantity) { this.quantity = quantity; }

    public boolean isDeleted() {return isDeleted;}

    public void setDeleted(boolean deleted) {isDeleted = deleted;}

    public Base getBase() { return base; }
    public void setBase(Base base) { this.base = base; }

}
