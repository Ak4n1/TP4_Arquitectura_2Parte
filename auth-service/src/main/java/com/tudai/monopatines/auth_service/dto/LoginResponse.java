package com.tudai.monopatines.auth_service.dto;

import java.util.List;

/**
 * DTO para la respuesta de login.
 */
public class LoginResponse {

    private String token;
    private Long userId;
    private String email;
    private List<String> roles;

    public LoginResponse() {
    }

    public LoginResponse(String token, Long userId, String email, List<String> roles) {
        this.token = token;
        this.userId = userId;
        this.email = email;
        this.roles = roles;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}

