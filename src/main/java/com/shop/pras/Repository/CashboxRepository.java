package com.shop.pras.Repository;

import com.shop.pras.Models.Cashbox;
import com.shop.pras.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CashboxRepository extends JpaRepository<Cashbox, String> {
    @Query("SELECT p FROM Cashbox p WHERE p.base.baseLogin = :baseLogin")
    List<Cashbox> getCashbox(String baseLogin);
}