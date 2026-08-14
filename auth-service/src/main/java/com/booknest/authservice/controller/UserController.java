package com.booknest.authservice.controller;

import com.booknest.authservice.model.User;
import com.booknest.authservice.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private AuthService service;

    @GetMapping("/{id}")
    public User getById(@PathVariable String id) {
        User u = service.getById(id);
        u.setPasswordHash(null);
        return u;
    }

    @PutMapping("/{id}/profile")
    public User updateProfile(@PathVariable String id, @RequestBody User u) {
        User updated = service.updateProfile(id, u);
        updated.setPasswordHash(null);
        return updated;
    }
}
