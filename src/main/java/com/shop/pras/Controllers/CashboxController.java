package com.shop.pras.Controllers;

import com.shop.pras.Models.Cashbox;
import com.shop.pras.Models.User;
import com.shop.pras.Services.CashboxService;
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
public class CashboxController {
    private final CashboxService cashboxService;

    public CashboxController(CashboxService cashboxService){
        this.cashboxService =cashboxService;
    }

    @PostMapping("/get/cashbox-balance")
    public ResponseEntity<?> getCashbox(@RequestBody Map<String, String> request){
        String login = request.get("baseEmail");

        List<Cashbox> cashboxes = cashboxService.getCashbox(login);

        if (cashboxes.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No users found");
        }

        Map<String, Object> response = new HashMap<>();
        response.put("cashboxes", cashboxes);

        System.out.println("DEBUG: Response body: " + response);
        return ResponseEntity.ok(response);
    }
}
