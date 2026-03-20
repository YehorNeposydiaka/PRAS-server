package com.shop.pras.Services;

import com.shop.pras.Models.Product;
import com.shop.pras.Repository.BaseRepository;
import com.shop.pras.Repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final BaseRepository baseRepository;

    public ProductService(ProductRepository productRepository, BaseRepository baseRepository) {
        this.productRepository = productRepository;
        this.baseRepository = baseRepository;
    }

    public List<Product> getAllProducts(String baseLogin) {
        return productRepository.getAllProducts(baseLogin);
    }

    public List<Product> searchByPart(String baseLogin, String search) {
        return productRepository.searchByPart(baseLogin, search);
    }

    @Transactional
    public void addOrUpdateProduct(String baseLogin, Product productDto) {
        baseRepository.findById(baseLogin).ifPresent(base -> {
            // Шукаємо ТІЛЬКИ серед активних, як ви просили
            List<Product> activeProducts = productRepository.findByCodeAndBase(productDto.getCode(), baseLogin);

            if (!activeProducts.isEmpty()) {
                // Оновлюємо перший знайдений активний товар
                Product existingProduct = activeProducts.get(0);

                existingProduct.setProductName(productDto.getProductName());
                existingProduct.setCost(productDto.getCost());
                existingProduct.setPrice(productDto.getPrice());
                existingProduct.setQuantity(productDto.getQuantity());
            } else {
                // Якщо активного не знайдено, створюємо новий
                productDto.setBase(base);
                productDto.setDeleted(false);
                productRepository.save(productDto);
            }
        });
    }

    @Transactional
    public void DeleteProduct(String code, String baseLogin) {
        List<Product> products = productRepository.findByCodeAndBase(code, baseLogin);
        if (!products.isEmpty()) {
            Product product = products.get(0);
            product.setDeleted(true); // Робимо його неактивним
        }
    }
}