package com.shop.pras.Repository;

import com.shop.pras.Models.Base;
import com.shop.pras.Models.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Integer> {

    /**
     * Отримуємо всі накладні конкретної бази.
     * Використовуємо JOIN FETCH для завантаження списку товарів (items) одним запитом.
     * DISTINCT гарантує, що ми не отримаємо дублікати накладних через зв'язок OneToMany.
     */
    @Query("SELECT DISTINCT i FROM Invoice i LEFT JOIN FETCH i.items " +
            "WHERE i.base = :base ORDER BY i.invoiceTime DESC")
    List<Invoice> findByBaseOrderByInvoiceTimeDesc(@Param("base") Base base);

    /**
     * Стандартний пошук без примусового завантаження товарів (якщо потрібно просто список без деталей)
     */
    List<Invoice> findByBase(Base base);

    /**
     * Пошук конкретної накладної за номером, типом та базою.
     * Використовується при додаванні товарів у вже існуючу накладну.
     */
    Optional<Invoice> findByInvoiceNumAndInvoiceTypeAndBase(int invoiceNum, String invoiceType, Base base);

    /**
     * Отримання максимального номера накладної для автоматичної інкрементації.
     * Повертає Integer (може бути null, якщо накладних цього типу ще немає).
     */
    @Query("SELECT MAX(i.invoiceNum) FROM Invoice i WHERE i.invoiceType = :type AND i.base = :base")
    Integer findMaxInvoiceNumByTypeAndBase(@Param("type") String type, @Param("base") Base base);

    /**
     * Фільтрація накладних за статусом (наприклад, "Збережена" або "Проведена")
     */
    @Query("SELECT DISTINCT i FROM Invoice i LEFT JOIN FETCH i.items " +
            "WHERE i.status = :status AND i.base = :base ORDER BY i.invoiceTime DESC")
    List<Invoice> findByStatusAndBase(@Param("status") String status, @Param("base") Base base);
}