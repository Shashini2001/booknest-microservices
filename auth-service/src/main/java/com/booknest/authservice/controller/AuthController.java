package com.booknest.authservice.controller;

import com.booknest.authservice.model.User;
import com.booknest.authservice.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService service;

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User u) {
        User created = service.register(u);
        created.setPasswordHash(null); // never echo the hash back
        return ResponseEntity.status(201).body(created);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Map<String, String> body) {
        String token = service.login(body.get("email"), body.get("password"));
        return ResponseEntity.ok(Map.of("token", token));
    }
}
