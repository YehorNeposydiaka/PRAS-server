package com.shop.pras.Services;

import com.shop.pras.Models.*;
import com.shop.pras.Repository.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final BaseRepository baseRepository;
    private final CashboxRepository cashboxRepository;

    public TransactionService(TransactionRepository transactionRepository, BaseRepository baseRepository, CashboxRepository cashboxRepository){
        this.transactionRepository = transactionRepository;
        this.baseRepository = baseRepository;
        this.cashboxRepository = cashboxRepository;
    }
    @Transactional
    public List<Transaction> getTransactions(String baseLogin, String from, String to, String cashierName, String paymentMethod, String operationType) {
        LocalDateTime start = LocalDateTime.parse(from);
        LocalDateTime end = LocalDateTime.parse(to);
        return transactionRepository.findByBaseAndDateRange(baseLogin, start, end, cashierName, paymentMethod, operationType);
    }
    @Transactional
    public void addTransaction(String baseLogin, Transaction transaction) {
        // 1. Знаходимо базу
        Base base = baseRepository.findById(baseLogin)
                .orElseThrow(() -> new RuntimeException("Базу не знайдено"));

        // 2. Отримуємо всі каси для цієї бази через ВАШ метод
        List<Cashbox> cashboxes = cashboxRepository.getCashbox(baseLogin);

        // 3. Знаходимо потрібну касу за назвою
        Cashbox cashbox = cashboxes.stream()
                .filter(c -> c.getCashboxName().equalsIgnoreCase(transaction.getCashBoxName()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Касу '" + transaction.getCashBoxName() + "' не знайдено в списку"));

        // 4. Оновлюємо баланс (логіка додавання суми)
        // Якщо транзакція має інший тип (наприклад, повернення), додайте сюди перевірку
        double newBalance;
        if(transaction.getOperationType().equalsIgnoreCase("Витрата") || transaction.getOperationType().equalsIgnoreCase("Повернення"))
            newBalance = cashbox.getBalance() - transaction.getOperationSum();
        else
            newBalance = cashbox.getBalance() + transaction.getOperationSum();

        cashbox.setBalance(newBalance);

        // 5. Зберігаємо оновлену касу
        cashboxRepository.save(cashbox);

        // 6. Зберігаємо транзакцію
        transaction.setBase(base);
        transactionRepository.save(transaction);
    }
}
