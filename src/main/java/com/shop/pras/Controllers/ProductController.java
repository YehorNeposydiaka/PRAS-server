package com.shop.pras.Controllers;

import com.shop.pras.Models.Product;
import com.shop.pras.Services.ProductService;
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
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService){
        this.productService = productService;
    }

    @PostMapping("/find-all-products")
    public ResponseEntity<?> getAllProducts(@RequestBody Map<String, String> request){
        String login = request.get("baseEmail");

        List<Product> products = productService.getAllProducts(login);

        if (products.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No products found");
        }

        Map<String, Object> response = new HashMap<>();
        response.put("products", products);

        System.out.println("DEBUG: Response body: " + response);
        return ResponseEntity.ok(response);
    }
    @PostMapping("/find-product")
    public ResponseEntity<?> findProduct(@RequestBody Map<String, String> request){
        String login = request.get("baseEmail");
        String search = request.get("search");

        List<Product> products = productService.searchByPart(login, search);

        if (products.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No products found");
        }

        Map<String, Object> response = new HashMap<>();
        response.put("products", products);

        return ResponseEntity.ok(response);
    }
    @PostMapping("/add-product")
    public ResponseEntity<?> addProduct(@RequestBody Map<String, String> request){
        String login = request.get("baseEmail");
        String code = request.get("code");
        String name = request.get("productName");
        double cost = Double.parseDouble(request.get("cost"));
        double price = Double.parseDouble(request.get("price"));
        double quantity = Double.parseDouble(request.get("quantity"));
        Product product = new Product(code, name, cost, price, quantity);

        productService.addOrUpdateProduct(login, product);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);

        return ResponseEntity.ok(response);
    }
    @PostMapping("/delete-product")
    public ResponseEntity<?> deleteProduct(@RequestBody Map<String, String> request){
        String login = request.get("baseEmail");
        String code = request.get("code");

        if (code == null || code.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Code is required");
        }

        // Викликаємо логічне видалення
        productService.DeleteProduct(code, login);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Product marked as deleted");

        return ResponseEntity.ok(response);
    }
}
