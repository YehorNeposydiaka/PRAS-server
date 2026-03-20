package com.shop.pras.Models;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "bases")
public class Base {

    @Id
    @Column(length = 50)
    private String baseLogin;

    @Column(nullable = false)
    private String baseName;

    @Column(nullable = false)
    private String basePassword;

    public Base() {}

    public Base(String login, String name, String password) {
        this.baseLogin = login;
        this.baseName = name;
        this.basePassword = password;
    }

    // Геттери и сеттери
    public String getBaseLogin() { return baseLogin; }
    public void setBaseLogin(String baseLogin) { this.baseLogin = baseLogin; }

    public String getBaseName() { return baseName; }
    public void setBaseName(String baseName) { this.baseName = baseName; }

    public String getBasePassword() { return basePassword; }
    public void setBasePassword(String basePassword) { this.basePassword = basePassword; }

}
