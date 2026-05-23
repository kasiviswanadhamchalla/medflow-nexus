package com.medflow.auth_service.dto.response;

public record AuthResponse(
    Long userId,
    String email,
    String firstName,
    String lastName,
    String role,
    String token
) {}