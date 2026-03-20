package com.shop.pras.Controllers;

import com.shop.pras.Models.Product;
import com.shop.pras.Models.Receipt;
import com.shop.pras.Services.ReceiptService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ReceiptController {
    private final ReceiptService receiptService;

    public ReceiptController(ReceiptService receiptService){
        this.receiptService = receiptService;
    }

    public static class ReceiptRequest {
        public String login;
        public Receipt receipt;
    }
    public static class ReceiptSearchRequest {
        public String baseEmail;
        public String from;
        public String to;
        public String cashierName;   // може бути null
        public String paymentMethod; // може бути null
        public Boolean isReturn;     // може бути null
    }
    @PostMapping("/add-receipt")
    public ResponseEntity<?> addReceipt(@RequestBody ReceiptRequest request) {
        // 1. Перевірка об'єкта запиту
        if (request == null || request.receipt == null) {
            return ResponseEntity.badRequest().body("Невірний формат JSON: об'єкт receipt відсутній");
        }

        // 2. ДЕБАГ: Перевіримо, чи взагалі прийшли товари
        if (request.receipt.getItems() == null) {
            System.out.println("ПОМИЛКА: Список товарів (items) дорівнює null!");
            return ResponseEntity.badRequest().body("Список товарів (items) не може бути порожнім");
        }

        System.out.println("Отримано чеків товарів: " + request.receipt.getCashierName());
        System.out.println("Чек повернення " + request.receipt.getIsReturn());
        receiptService.addReceipt(request.login, request.receipt);

        return ResponseEntity.ok(Map.of("success", true));
    }
    @PostMapping("/get-all-receipts")
    public ResponseEntity<?> findComplex(@RequestBody ReceiptSearchRequest req){
        List<Receipt> receipts = receiptService.getAllReceipts(
                req.baseEmail, req.from, req.to, req.cashierName, req.paymentMethod, req.isReturn
        );
        return ResponseEntity.ok(Map.of("receipts", receipts));
    }
}
