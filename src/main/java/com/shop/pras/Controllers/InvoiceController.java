package com.shop.pras.Controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.shop.pras.Models.Invoice;
import com.shop.pras.Models.ReceiptItem;
import com.shop.pras.Services.InvoiceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class InvoiceController {

    private final InvoiceService invoiceService;
    private final ObjectMapper mapper;

    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
        // Налаштовуємо мапер для коректної обробки дати/часу в JSON
        this.mapper = new ObjectMapper();
        this.mapper.registerModule(new JavaTimeModule());
    }

    @PostMapping("/get-all-invoices")
    public ResponseEntity<?> getAllInvoices(@RequestBody Map<String, String> request) {
        try {
            String email = request.get("baseEmail");
            if (email == null) {
                return ResponseEntity.badRequest().body("baseEmail є обов'язковим");
            }

            // Отримуємо список накладних через сервіс
            List<Invoice> invoices = invoiceService.getAllInvoices(email);

            // Повертаємо список об'єктів (Spring автоматично конвертує List в JSON масив)
            return ResponseEntity.ok(invoices);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Помилка отримання списку накладних: " + e.getMessage());
        }
    }
    /**
     * Отримання останнього номера накладної для конкретної бази та типу.
     * Використовується клієнтом для генерації наступного номера (last + 1).
     */
    @PostMapping("/get-last-invoice-number")
    public ResponseEntity<?> getLastInvoiceNumber(@RequestBody Map<String, String> request) {
        try {
            String email = request.get("baseEmail");
            String type = request.get("type");

            if (email == null || type == null) {
                return ResponseEntity.badRequest().body("baseEmail та type є обов'язковими");
            }

            Integer lastNum = invoiceService.getLastNumber(email, type);

            Map<String, Object> response = new HashMap<>();
            response.put("lastNum", lastNum);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    /**
     * Створення нової накладної (голови документа) зі статусом "Збережена".
     */
    @PostMapping("/add-invoice")
    public ResponseEntity<?> addInvoice(@RequestBody Invoice invoice) {
        try {
            // userName в об'єкті Invoice має містити baseEmail (логін бази)
            invoiceService.addInvoice(invoice);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Помилка створення накладної: " + e.getMessage());
        }
    }

    /**
     * Додавання товару до вже існуючої накладної.
     */
    @PostMapping("/add-product-to-invoice")
    public ResponseEntity<?> addProductToInvoice(@RequestBody Map<String, Object> request) {
        try {
            int invoiceNum = (int) request.get("invoiceNum");
            String type = (String) request.get("type");
            String baseEmail = (String) request.get("baseEmail");

            // Конвертуємо частину Map назад в об'єкт ReceiptItem
            ReceiptItem item = mapper.convertValue(request.get("item"), ReceiptItem.class);

            invoiceService.addProductToInvoice(invoiceNum, type, baseEmail, item);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Помилка додавання товару: " + e.getMessage());
        }
    }

    /**
     * Проведення накладної: зміна статусу на "Проведена" та оновлення залишків/цін товарів.
     * Викликається клієнтом при натисканні кнопки "ПРОВЕСТИ".
     */
    @PostMapping("/commit-invoice")
    public ResponseEntity<?> commitInvoice(@RequestBody Map<String, Object> request) {
        try {
            int invoiceNum = (int) request.get("invoiceNum");
            String type = (String) request.get("type");
            String baseEmail = (String) request.get("baseEmail");

            if (baseEmail == null) {
                return ResponseEntity.badRequest().body("baseEmail не вказано");
            }

            invoiceService.commitInvoice(invoiceNum, type, baseEmail);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Помилка проведення: " + e.getMessage());
        }
    }
}