package com.booknest.authservice.controller;

import com.booknest.authservice.dto.AuthResponse;
import com.booknest.authservice.dto.LoginRequest;
import com.booknest.authservice.dto.RegisterRequest;
import com.booknest.authservice.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            AuthResponse response = authService.register(request);
            return ResponseEntity.status(201).body(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            AuthResponse response = authService.login(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }
}
