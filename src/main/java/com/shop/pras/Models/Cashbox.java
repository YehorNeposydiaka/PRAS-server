package com.shop.pras.Models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

@Entity
@Table(name="cashbox")

public class Cashbox {
    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long cashboxId;

    @Column(nullable = false)
    private String cashboxName;

    @Column(nullable = false)
    private double balance;

    @ManyToOne
    @JoinColumn(
            name = "base_login",              // колонка в users
            referencedColumnName = "baseLogin", // PK в bases
            nullable = false
    )
    private Base base;

    //Constructors
    public Cashbox(){}

    public Cashbox(String cashboxName, double balance){
        this.cashboxName = cashboxName;
        this.balance = balance;
    }

   public void setCashboxName(String cashboxName){this.cashboxName = cashboxName;}
    public void setBalance(double balance){this.balance = balance;}
    public void setBase(Base base) {
        this.base = base;
    }

    //Getters
    public long getCashboxId() {
        return cashboxId;
    }
    public String getCashboxName() {
        return cashboxName;
    }
    public double getBalance(){return balance;}
    public Base getBase() {
        return base;
    }
}
