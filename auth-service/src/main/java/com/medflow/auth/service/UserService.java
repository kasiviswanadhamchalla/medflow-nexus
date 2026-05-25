package com.medflow.auth.service;

import com.medflow.auth.dto.response.UserResponse;

import java.util.List;

public interface UserService {
    UserResponse getUserProfile(String username);
    List<UserResponse> getAllUsers();
    void deleteUser(Long id);
}
