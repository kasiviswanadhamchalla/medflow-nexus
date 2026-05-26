package com.microservices.auth_service.service;

import com.microservices.auth_service.dto.request.ChangePasswordRequest;
import com.microservices.auth_service.dto.request.LoginRequest;
import com.microservices.auth_service.dto.request.RefreshTokenRequest;
import com.microservices.auth_service.dto.request.RegisterRequest;
import com.microservices.auth_service.dto.response.AuthResponse;
import com.microservices.auth_service.dto.response.MessageResponse;

public interface AuthService {
    MessageResponse registerUser(RegisterRequest registerRequest);
    AuthResponse authenticateUser(LoginRequest loginRequest);
    AuthResponse refreshToken(RefreshTokenRequest refreshTokenRequest);
    MessageResponse changePassword(String username, ChangePasswordRequest changePasswordRequest);
}
