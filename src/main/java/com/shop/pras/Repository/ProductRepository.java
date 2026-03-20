package com.shop.pras.Repository;

import com.shop.pras.Models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {

    // Пошук тільки активних товарів (isDeleted = false)
    @Query("SELECT p FROM Product p WHERE p.base.baseLogin = :baseLogin " +
            "AND p.isDeleted = false " +
            "AND (p.code LIKE %:search% OR p.productName LIKE %:search%)")
    List<Product> searchByPart(@Param("baseLogin") String baseLogin, @Param("search") String search);

    // Отримання всіх активних товарів
    @Query("SELECT p FROM Product p WHERE p.base.baseLogin = :baseLogin AND p.isDeleted = false")
    List<Product> getAllProducts(@Param("baseLogin") String baseLogin);

    // Пошук товару за кодом (активного)
    @Query("SELECT p FROM Product p WHERE p.code = :code AND p.base.baseLogin = :baseLogin AND p.isDeleted = false")
    List<Product> findByCodeAndBase(@Param("code") String code, @Param("baseLogin") String baseLogin);

}