package com.medflow.auth_service.security.jwt;

public interface JwtTokenProvider {
    String generateToken(Long userId, String email, String role);
    Long getUserIdFromToken(String token);
    String getEmailFromToken(String token);
    String getRoleFromToken(String token);
    boolean validateToken(String token);
}