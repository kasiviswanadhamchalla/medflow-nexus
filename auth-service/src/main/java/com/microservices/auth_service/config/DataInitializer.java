package com.microservices.auth_service.config;

import com.microservices.auth_service.entity.ERole;
import com.microservices.auth_service.entity.Role;
import com.microservices.auth_service.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private final RoleRepository roleRepository;

    @Bean
    public CommandLineRunner initData() {
        return args -> {
            if (roleRepository.count() == 0) {
                roleRepository.save(Role.builder().name(ERole.ADMIN).description("Administrator").build());
                roleRepository.save(Role.builder().name(ERole.DOCTOR).description("Doctor").build());
                roleRepository.save(Role.builder().name(ERole.PATIENT).description("Patient").build());
                System.out.println("Roles initialized in database.");
            }
        };
    }
}
