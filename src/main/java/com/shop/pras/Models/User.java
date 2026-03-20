package com.shop.pras.Models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

@Entity
@Table(name="users")

public class User {
    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long userId;

    @Column(nullable = false)
    private String userName;

    @Column(nullable = false)
    private String userPassword;

    private boolean isAdmin;

    @ManyToOne
    @JoinColumn(
            name = "base_login",              // колонка в users
            referencedColumnName = "baseLogin", // PK в bases
            nullable = false
    )
    private Base base;

//Constructors
    public User(){}

    public User(String name, String password, boolean isAdm){
        this.userName = name;
        this.userPassword = password;
        this.isAdmin = isAdm;
    }

    //Setters
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public void setUserPassword(String password) {
        this.userPassword = password;
    }
    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }
    public void setBase(Base base) {
        this.base = base;
    }

    //Getters
    public long getUserId() {
        return userId;
    }
    public String getUserName() {
        return userName;
    }
    public String getUserPassword() {
        return userPassword;
    }
    @JsonProperty("isAdmin")
    public boolean getRole(){
        return isAdmin;
    }
    public Base getBase() {
        return base;
    }
}
