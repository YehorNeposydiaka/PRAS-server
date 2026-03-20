package com.shop.pras.Controllers;

import com.shop.pras.Models.Cashbox;
import com.shop.pras.Models.Receipt;
import com.shop.pras.Models.Transaction;
import com.shop.pras.Models.User;
import com.shop.pras.Services.CashboxService;
import com.shop.pras.Services.TransactionService;
import com.shop.pras.Services.UserService;
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
public class TransactionController {
    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService){
        this.transactionService =transactionService;
    }

    public static class TransactionRequest {
        public String login;
        public Transaction transaction;
    }
    public static class TransactionSearchRequest{
        public String baseEmail;
        public String from;
        public String to;
        public String cashierName;
        public String paymentMethod;
        public String operationType;
    }
    @PostMapping("/get-all-transactions")
    public ResponseEntity<?> getTransactions(@RequestBody TransactionController.TransactionSearchRequest req){
        List<Transaction> transactions = transactionService.getTransactions(
                req.baseEmail, req.from, req.to, req.cashierName, req.paymentMethod, req.operationType
        );
        return ResponseEntity.ok(Map.of("transactions", transactions));
    }

    @PostMapping("/add/transaction")
    public ResponseEntity<?> addTransaction(@RequestBody TransactionRequest request){
        if (request == null || request.transaction == null || request.login == null) {
            return ResponseEntity.badRequest().body("Невірні дані транзакції");
        }

        System.out.println("Отримано транзакцію для каси: " + request.transaction.getCashBoxName());

        transactionService.addTransaction(request.login, request.transaction);

        return ResponseEntity.ok(Map.of("success", true));
    }
}
