package com.microservices.auth_service.service.impl;

import com.microservices.auth_service.dto.request.ChangePasswordRequest;
import com.microservices.auth_service.dto.request.LoginRequest;
import com.microservices.auth_service.dto.request.RefreshTokenRequest;
import com.microservices.auth_service.dto.request.RegisterRequest;
import com.microservices.auth_service.dto.response.AuthResponse;
import com.microservices.auth_service.dto.response.MessageResponse;
import com.microservices.auth_service.entity.ERole;
import com.microservices.auth_service.entity.RefreshToken;
import com.microservices.auth_service.entity.Role;
import com.microservices.auth_service.entity.User;
import com.microservices.auth_service.exception.TokenRefreshException;
import com.microservices.auth_service.repository.RoleRepository;
import com.microservices.auth_service.repository.UserRepository;
import com.microservices.auth_service.security.jwt.JwtUtils;
import com.microservices.auth_service.security.services.UserDetailsImpl;
import com.microservices.auth_service.service.AuthService;
import com.microservices.auth_service.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;
    private final JwtUtils jwtUtils;
    private final RefreshTokenService refreshTokenService;

    @Override
    @Transactional
    public MessageResponse registerUser(RegisterRequest registerRequest) {
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            return new MessageResponse("Error: Username is already taken!");
        }

        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            return new MessageResponse("Error: Email is already in use!");
        }

        // Create new user's account
        User user = User.builder()
                .username(registerRequest.getUsername())
                .email(registerRequest.getEmail())
                .password(encoder.encode(registerRequest.getPassword()))
                .enabled(true)
                .build();

        Set<String> strRoles = registerRequest.getRoles();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null || strRoles.isEmpty()) {
            Role patientRole = roleRepository.findByName(ERole.PATIENT)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(patientRole);
        } else {
            strRoles.forEach(role -> {
                switch (role.toLowerCase()) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);
                        break;
                    case "doctor":
                        Role doctorRole = roleRepository.findByName(ERole.DOCTOR)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(doctorRole);
                        break;
                    default:
                        Role userRole = roleRepository.findByName(ERole.PATIENT)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }

        user.setRoles(roles);
        userRepository.save(user);

        return new MessageResponse("User registered successfully!");
    }

    @Override
    public AuthResponse authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());

        return AuthResponse.builder()
                .token(jwt)
                .refreshToken(refreshToken.getToken())
                .id(userDetails.getId())
                .username(userDetails.getUsername())
                .email(userDetails.getEmail())
                .roles(roles)
                .type("Bearer")
                .build();
    }

    @Override
    public AuthResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        String requestRefreshToken = refreshTokenRequest.getRefreshToken();

        return refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String token = jwtUtils.generateTokenFromUsername(user.getUsername());
                    return AuthResponse.builder()
                            .token(token)
                            .refreshToken(requestRefreshToken)
                            .id(user.getId())
                            .username(user.getUsername())
                            .email(user.getEmail())
                            .roles(user.getRoles().stream().map(role -> role.getName().name()).collect(Collectors.toList()))
                            .type("Bearer")
                            .build();
                })
                .orElseThrow(() -> new TokenRefreshException(requestRefreshToken, "Refresh token is not in database!"));
    }

    @Override
    @Transactional
    public MessageResponse changePassword(String username, ChangePasswordRequest changePasswordRequest) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Error: User not found."));

        if (!encoder.matches(changePasswordRequest.getOldPassword(), user.getPassword())) {
            return new MessageResponse("Error: Invalid old password.");
        }

        user.setPassword(encoder.encode(changePasswordRequest.getNewPassword()));
        userRepository.save(user);

        return new MessageResponse("Password changed successfully!");
    }
}
