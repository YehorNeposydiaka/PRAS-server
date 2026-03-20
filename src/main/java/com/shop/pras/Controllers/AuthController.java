package com.shop.pras.Controllers;

import com.shop.pras.Models.Base;
import com.shop.pras.Models.User;
import com.shop.pras.Services.BaseService;
import com.shop.pras.Services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final BaseService baseService;
    private final UserService userService;

    public AuthController(BaseService baseService, UserService userService){
        this.baseService = baseService;
        this.userService = userService;
    }

    @PostMapping("/verify-base")
    public ResponseEntity<?> verifyBase(@RequestBody Map<String, String> request) {
        String login = request.get("baseEmail");
        String password = request.get("basePassword");

        if (login.isBlank() || password.isBlank()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login or password are blank");
        }

        Optional<Base> baseOpt = baseService.findByLogin(login);

        if (baseOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No bases with this login");
        }

        Base base = baseOpt.get();

        if (!base.getBasePassword().equals(password)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Wrong password");
        }
        return ResponseEntity.ok(Map.of("userName", login));
    }
    @PostMapping("/verify-local-user")
    public ResponseEntity<?> verifyUser(@RequestBody Map<String, String> request) {
        String login = request.get("baseEmail");
        String password = request.get("userPassword");

        Optional<User> userOpt = userService.findUserByPasswordForBase(password, login);

        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No user with this password");
        }

        User user = userOpt.get();


        return ResponseEntity.ok(Map.of(
                "userName", user.getUserName(),
                "isAdmin", user.getRole()
        ));
    }

    @PostMapping("/get-base-name")
    public ResponseEntity<?> baseName(@RequestBody Map<String, String> request) {
        String login = request.get("baseEmail");

        if (login.isBlank()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login or password are blank");
        }

        Optional<Base> baseOpt = baseService.findByLogin(login);

        if (baseOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No bases with this login");
        }

        Base base = baseOpt.get();

        return ResponseEntity.ok(Map.of("baseName", base.getBaseName()));
    }
}