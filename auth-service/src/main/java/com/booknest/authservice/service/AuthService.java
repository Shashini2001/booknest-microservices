package com.booknest.authservice.service;

import com.booknest.authservice.dto.AuthResponse;
import com.booknest.authservice.dto.LoginRequest;
import com.booknest.authservice.dto.RegisterRequest;
import com.booknest.authservice.model.User;
import com.booknest.authservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        User user = new User();
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setRole("CUSTOMER");

        User saved = userRepository.save(user);
        String token = jwtService.generateToken(saved.getId(), saved.getEmail(), saved.getRole());

        return new AuthResponse(token, saved.getId(), saved.getFullName(), saved.getEmail(), saved.getRole());
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new RuntimeException("Invalid email or password");
        }

        String token = jwtService.generateToken(user.getId(), user.getEmail(), user.getRole());
        return new AuthResponse(token, user.getId(), user.getFullName(), user.getEmail(), user.getRole());
    }
}
