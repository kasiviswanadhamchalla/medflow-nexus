package com.medflow.auth.service;

import com.medflow.auth.dto.request.ChangePasswordRequest;
import com.medflow.auth.dto.request.LoginRequest;
import com.medflow.auth.dto.request.RefreshTokenRequest;
import com.medflow.auth.dto.request.RegisterRequest;
import com.medflow.auth.dto.response.AuthResponse;
import com.medflow.auth.dto.response.MessageResponse;

public interface AuthService {
    MessageResponse registerUser(RegisterRequest registerRequest);
    AuthResponse authenticateUser(LoginRequest loginRequest);
    AuthResponse refreshToken(RefreshTokenRequest refreshTokenRequest);
    MessageResponse changePassword(String username, ChangePasswordRequest changePasswordRequest);
}
