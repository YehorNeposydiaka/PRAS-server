package com.shop.pras.Controllers;

import com.shop.pras.Models.User;
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
public class UserController {
    private final UserService userService;

    public UserController(UserService userService){
        this.userService =userService;
    }

    @PostMapping("/find-all-users")
    public ResponseEntity<?> getAllProducts(@RequestBody Map<String, String> request){
        String login = request.get("baseEmail");

        List<User> users = userService.getAllUsers(login);

        if (users.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No users found");
        }

        Map<String, Object> response = new HashMap<>();
        response.put("users", users);

        System.out.println("DEBUG: Response body: " + response);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/add-user")
    public ResponseEntity<?> addUser(@RequestBody Map<String, String> request){
        String login = request.get("baseEmail");
        String userName = request.get("userName");
        String userPassword = request.get("userPassword");
        boolean isAdmin = Boolean.parseBoolean(request.get("isAdmin"));
        User user = new User(userName, userPassword, isAdmin);

        userService.addUser(login, user);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);

        return ResponseEntity.ok(response);
    }
}
