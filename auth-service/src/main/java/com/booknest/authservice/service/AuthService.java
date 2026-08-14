package com.booknest.authservice.service;

import com.booknest.authservice.model.User;
import com.booknest.authservice.repository.UserRepository;
import com.booknest.authservice.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository repo;

    @Autowired
    private JwtUtil jwtUtil;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public User register(User u) {
        if (repo.findByEmail(u.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already registered");
        }
        u.setPasswordHash(encoder.encode(u.getPasswordHash())); // raw password arrives in this field
        u.setRole("USER");
        return repo.save(u);
    }

    public String login(String email, String rawPassword) {
        User u = repo.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));
        if (!encoder.matches(rawPassword, u.getPasswordHash())) {
            throw new IllegalArgumentException("Invalid credentials");
        }
        return jwtUtil.generateToken(u.getId(), u.getEmail(), u.getRole());
    }

    public User getById(String id) {
        return repo.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    public User updateProfile(String id, User u) {
        User existing = getById(id);
        existing.setFullName(u.getFullName());
        // email/password changes intentionally excluded from this endpoint - keep those separate
        return repo.save(existing);
    }
}
