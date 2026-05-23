package com.medflow.auth_service.service;

import com.medflow.auth_service.dto.request.LoginRequest;
import com.medflow.auth_service.dto.request.RegisterRequest;
import com.medflow.auth_service.dto.response.AuthResponse;

public interface AuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
    void validateToken(String token);
}