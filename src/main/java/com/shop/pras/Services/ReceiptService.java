package com.shop.pras.Services;

import com.shop.pras.Models.Base;
import com.shop.pras.Models.Product;
import com.shop.pras.Models.Receipt;
import com.shop.pras.Models.ReceiptItem;
import com.shop.pras.Repository.BaseRepository;
import com.shop.pras.Repository.ProductRepository;
import com.shop.pras.Repository.ReceiptRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReceiptService {
    private final ReceiptRepository receiptRepository;
    private final BaseRepository baseRepository;
    private final ProductRepository productRepository;

    public ReceiptService(ReceiptRepository receiptRepository, BaseRepository baseRepository, ProductRepository productRepository){
        this.receiptRepository = receiptRepository;
        this.baseRepository = baseRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public List<Receipt> getAllReceipts(String baseLogin, String from, String to,
                                        String cashierName, String paymentMethod, Boolean isReturn) {
        LocalDateTime start = LocalDateTime.parse(from);
        LocalDateTime end = LocalDateTime.parse(to);

        // Викликаємо оновлений метод репозиторію з усіма фільтрами
        return receiptRepository.findComplex(baseLogin, start, end, cashierName, paymentMethod, isReturn);
    }

    @Transactional
    public void addReceipt(String baseLogin, Receipt receipt) {
        Base base = baseRepository.findById(baseLogin)
                .orElseThrow(() -> new RuntimeException("Базу не знайдено"));

        receipt.setBase(base);

        List<ReceiptItem> itemsFromRequest = receipt.getItems();
        receipt.setItems(new ArrayList<>());

        for (ReceiptItem itemFromClient : itemsFromRequest) {
            if (itemFromClient.getProduct() == null || itemFromClient.getProduct().getCode() == null) {
                throw new RuntimeException("Помилка: У позиції чека не вказано товар або його код!");
            }

            List<Product> products = productRepository.findByCodeAndBase(itemFromClient.getProduct().getCode(), baseLogin);

            if (products == null || products.isEmpty()) {
                throw new RuntimeException("Товар " + itemFromClient.getProduct().getCode() + " не знайдено");
            }

            Product product = products.get(0);
            ReceiptItem newItem = new ReceiptItem(product, itemFromClient.getQuantity());

            receipt.addItem(newItem);

            if(!receipt.getIsReturn())
                product.setQuantity(product.getQuantity() - newItem.getQuantity());
            else
                product.setQuantity(product.getQuantity() + newItem.getQuantity());
        }

        receiptRepository.save(receipt);
    }
}