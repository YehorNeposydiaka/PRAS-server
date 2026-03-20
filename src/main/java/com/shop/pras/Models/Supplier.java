package com.shop.pras.Models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

@Entity
@Table(name="suppliers")

public class Supplier {
    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long supplierId;

    @Column(nullable = false)
    private String supplierName;

    private String additionalInfo;

    private boolean isDeleted;

    @ManyToOne
    @JoinColumn(
            name = "base_login",              // колонка в users
            referencedColumnName = "baseLogin", // PK в bases
            nullable = false
    )
    private Base base;

    //Constructors
    public Supplier(){}

    public Supplier(String supplierName, String additionalInfo, boolean isDeleted){
        this.supplierName = supplierName;
        this.additionalInfo = additionalInfo;
        this.isDeleted = isDeleted;
    }

    public String getSupplierName() {return supplierName;}
    public void setSupplierName(String supplierName) {this.supplierName = supplierName;}

    public String getAdditionalInfo() {return additionalInfo;}
    public void setAdditionalInfo(String additionalInfo) {this.additionalInfo = additionalInfo;}

    public boolean isDeleted() {return isDeleted;}
    public void setDeleted(boolean deleted) {isDeleted = deleted;}

    public Base getBase() {return base;}

    public void setBase(Base base) {this.base = base;}
}
