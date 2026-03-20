package com.shop.pras.Controllers;

import com.shop.pras.Models.Supplier;
import com.shop.pras.Services.SupplierService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class SupplierController {

    private final SupplierService supplierService;

    public SupplierController(SupplierService supplierService) {
        this.supplierService = supplierService;
    }

    @PostMapping("/find-all-suppliers")
    public ResponseEntity<?> getAllSuppliers(@RequestBody Map<String, String> request) {
        String login = request.get("baseEmail");
        List<Supplier> suppliers = supplierService.getAllSuppliers(login);

        Map<String, Object> response = new HashMap<>();
        response.put("suppliers", suppliers);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/add-supplier")
    public ResponseEntity<?> addSupplier(@RequestBody Map<String, String> request) {
        String login = request.get("baseEmail");
        String name = request.get("supplierName");
        String addInfo = request.get("additionalInfo");

        if (name == null || name.trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Name is required");
        }

        Supplier supplier = new Supplier();
        supplier.setSupplierName(name);
        supplier.setAdditionalInfo(addInfo);

        supplierService.addOrUpdateSupplier(login, supplier);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/delete-supplier")
    public ResponseEntity<?> deleteSupplier(@RequestBody Map<String, String> request) {
        String login = request.get("baseEmail");
        String name = request.get("supplierName");

        supplierService.DeleteSupplier(name, login);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        return ResponseEntity.ok(response);
    }
}