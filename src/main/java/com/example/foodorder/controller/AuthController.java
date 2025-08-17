package com.example.foodorder.controller;

import com.example.foodorder.model.User;
import com.example.foodorder.repository.UserRepo;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserRepo repo;
    private final PasswordEncoder encoder;

    public AuthController(UserRepo repo, PasswordEncoder encoder) {
        this.repo = repo;
        this.encoder = encoder;
    }

    @PostMapping("/register")
    public String register(@RequestBody User user) {
        user.setPassword(encoder.encode(user.getPassword()));
        if (user.getRole() == null) {
            user.setRole("USER"); // default role
        }
        repo.save(user);
        return "User registered successfully";
    }

    @PostMapping("/login")
    public String login() {
        // With HTTP Basic, login is handled by Spring Security itself.
        return "Login successful";
    }
}
