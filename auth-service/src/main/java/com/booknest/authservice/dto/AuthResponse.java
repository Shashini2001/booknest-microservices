package com.booknest.authservice.dto;

public class AuthResponse {
    private String token;
    private String userId;
    private String fullName;
    private String email;
    private String role;

    public AuthResponse(String token, String userId, String fullName, String email, String role) {
        this.token = token;
        this.userId = userId;
        this.fullName = fullName;
        this.email = email;
        this.role = role;
    }

    public String getToken() { return token; }
    public String getUserId() { return userId; }
    public String getFullName() { return fullName; }
    public String getEmail() { return email; }
    public String getRole() { return role; }
}
