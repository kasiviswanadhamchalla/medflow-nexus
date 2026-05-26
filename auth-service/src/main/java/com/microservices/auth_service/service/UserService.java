package com.microservices.auth_service.service;

import com.microservices.auth_service.dto.response.UserResponse;

import java.util.List;

public interface UserService {
    UserResponse getUserProfile(String username);
    List<UserResponse> getAllUsers();
    void deleteUser(Long id);
}
