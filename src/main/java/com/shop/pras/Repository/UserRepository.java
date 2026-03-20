package com.shop.pras.Repository;

import com.shop.pras.Models.Product;
import com.shop.pras.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // Проверка пользователя по паролю и базе
    Optional<User> findByUserPasswordAndBase_BaseLogin(
            String userPassword,
            String baseLogin
    );
    @Query("SELECT p FROM User p WHERE p.base.baseLogin = :baseLogin")
    List<User> getAllUsers(String baseLogin);
}
