package com.shop.pras.Repository;

import com.shop.pras.Models.Product;
import com.shop.pras.Models.Supplier;
import com.shop.pras.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SupplierRepository extends JpaRepository<Supplier, Long> {

    @Query("SELECT s FROM Supplier s WHERE s.base.baseLogin = :baseLogin AND s.isDeleted = false")
    List<Supplier> getAllSuppliers(@Param("baseLogin") String baseLogin);

    // Додаємо пошук за назвою + базою
    @Query("SELECT s FROM Supplier s WHERE s.supplierName = :name AND s.base.baseLogin = :baseLogin AND s.isDeleted = false")
    Optional<Supplier> findByNameAndBase(@Param("name") String name, @Param("baseLogin") String baseLogin);
}
