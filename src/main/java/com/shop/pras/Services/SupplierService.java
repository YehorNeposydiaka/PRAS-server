package com.shop.pras.Services;

import com.shop.pras.Models.Product;
import com.shop.pras.Models.Supplier;
import com.shop.pras.Repository.BaseRepository;
import com.shop.pras.Repository.ProductRepository;
import com.shop.pras.Repository.SupplierRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SupplierService {

    private final SupplierRepository supplierRepository;
    private final BaseRepository baseRepository;

    public SupplierService(SupplierRepository supplierRepository, BaseRepository baseRepository) {
        this.supplierRepository = supplierRepository;
        this.baseRepository = baseRepository;
    }
    @Transactional
    public List<Supplier> getAllSuppliers(String baseLogin) {
        return supplierRepository.getAllSuppliers(baseLogin);
    }
    @Transactional
    public void addOrUpdateSupplier(String baseLogin, Supplier supplierDto) {
        baseRepository.findById(baseLogin).ifPresent(base -> {
            // Шукаємо за назвою в конкретній базі
            Optional<Supplier> existingSupplier = supplierRepository.findByNameAndBase(supplierDto.getSupplierName(), baseLogin);

            if (existingSupplier.isPresent()) {
                // Якщо є — оновлюємо дані (можна додати оновлення телефону, адреси тощо)
                Supplier supplier = existingSupplier.get();
                supplier.setSupplierName(supplierDto.getSupplierName());
            } else {
                // Якщо немає — створюємо нового
                supplierDto.setBase(base);
                supplierDto.setDeleted(false);
                supplierRepository.save(supplierDto);
            }
        });
    }

    @Transactional
    public void DeleteSupplier(String supplierName, String baseLogin) {
        supplierRepository.findByNameAndBase(supplierName, baseLogin).ifPresent(supplier -> {
            supplier.setDeleted(true);
        });
    }
}