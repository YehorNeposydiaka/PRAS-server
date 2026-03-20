package com.shop.pras.Services;

import com.shop.pras.Models.Base;
import com.shop.pras.Models.Invoice;
import com.shop.pras.Models.Product;
import com.shop.pras.Models.ReceiptItem;
import com.shop.pras.Repository.BaseRepository;
import com.shop.pras.Repository.InvoiceRepository;
import com.shop.pras.Repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InvoiceService {
    private final InvoiceRepository invoiceRepository;
    private final BaseRepository baseRepository;
    private final ProductRepository productRepository;

    public InvoiceService(InvoiceRepository invoiceRepository,
                          BaseRepository baseRepository,
                          ProductRepository productRepository) {
        this.invoiceRepository = invoiceRepository;
        this.baseRepository = baseRepository;
        this.productRepository = productRepository;
    }

    /**
     * Отримання останнього номера накладної для клієнта
     */
    public Integer getLastNumber(String baseLogin, String type) {
        Base base = baseRepository.findById(baseLogin)
                .orElseThrow(() -> new RuntimeException("Базу " + baseLogin + " не знайдено"));

        Integer lastNum = invoiceRepository.findMaxInvoiceNumByTypeAndBase(type, base);
        return lastNum != null ? lastNum : 0;
    }

    /**
     * Створення "голови" накладної (статус "Збережена")
     */
    @Transactional
    public void addInvoice(Invoice invoice) {
        // Використовуємо ідентифікатор бази, який передано в userName (логін)
        Base base = baseRepository.findById(invoice.getUserName())
                .orElseThrow(() -> new RuntimeException("Базу " + invoice.getUserName() + " не знайдено"));

        invoice.setBase(base);
        invoice.setStatus("Збережена");
        invoiceRepository.save(invoice);
    }
    public List<Invoice> getAllInvoices(String baseLogin) {
        Base base = baseRepository.findById(baseLogin)
                .orElseThrow(() -> new RuntimeException("Базу " + baseLogin + " не знайдено"));

        // Припускаємо, що в репозиторії є метод пошуку за об'єктом Base
        return invoiceRepository.findByBaseOrderByInvoiceTimeDesc(base);
    }
    /**
     * Додавання товару до існуючої накладної
     */
    @Transactional
    public void addProductToInvoice(int invoiceNum, String type, String baseLogin, ReceiptItem itemDto) {
        Base base = baseRepository.findById(baseLogin)
                .orElseThrow(() -> new RuntimeException("Базу не знайдено"));

        Invoice invoice = invoiceRepository.findByInvoiceNumAndInvoiceTypeAndBase(invoiceNum, type, base)
                .orElseThrow(() -> new RuntimeException("Накладну не знайдено"));

        // Пошук товару аналогічно до ProductService
        List<Product> products = productRepository.findByCodeAndBase(itemDto.getProduct().getCode(), baseLogin);
        if (products.isEmpty()) {
            throw new RuntimeException("Товар з кодом " + itemDto.getProduct().getCode() + " не знайдено");
        }

        Product product = products.get(0);

        // Створюємо нову позицію
        ReceiptItem newItem = new ReceiptItem();
        newItem.setProduct(product);
        newItem.setQuantity(itemDto.getQuantity());

        // Передаємо ціни з клієнта в об'єкт товару (вони збережуться в ReceiptItem, якщо там є зв'язок)
        // Примітка: ціни в самій таблиці Product оновляться лише після методу commitInvoice
        product.setCost(itemDto.getProduct().getCost());
        product.setPrice(itemDto.getProduct().getPrice());

        invoice.addItem(newItem);
        invoiceRepository.save(invoice);
    }

    /**
     * Зміна статусу на "Проведена" та оновлення залишків/цін
     */
    @Transactional
    public void commitInvoice(int invoiceNum, String type, String baseLogin) {
        Base base = baseRepository.findById(baseLogin)
                .orElseThrow(() -> new RuntimeException("Базу не знайдено"));

        Invoice invoice = invoiceRepository.findByInvoiceNumAndInvoiceTypeAndBase(invoiceNum, type, base)
                .orElseThrow(() -> new RuntimeException("Накладну не знайдено"));

        if ("Проведена".equals(invoice.getStatus())) {
            throw new RuntimeException("Накладна №" + invoiceNum + " вже проведена!");
        }

        // Обробка тільки для ПРИХІДНОЇ накладної
        if ("INCOMING".equalsIgnoreCase(invoice.getInvoiceType())) {
            for (ReceiptItem item : invoice.getItems()) {
                Product product = item.getProduct();

                // 1. Додаємо кількість до існуючої на складі
                product.setQuantity(product.getQuantity() + item.getQuantity());

                // 2. Оновлюємо фінансові показники товару
                // Беремо ціни, які були вказані саме в цій накладній
                product.setCost(item.getProduct().getCost());
                product.setPrice(item.getProduct().getPrice());

                productRepository.save(product);
            }
        }

        invoice.setStatus("Проведена");
        invoiceRepository.save(invoice);
    }
}