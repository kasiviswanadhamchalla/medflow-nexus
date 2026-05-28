package com.microservices.auth_service.mapper;

import com.microservices.auth_service.dto.response.UserResponse;
import com.microservices.auth_service.entity.Role;
import com.microservices.auth_service.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "role", source = "role", qualifiedByName = "mapRole")
    UserResponse toResponse(User user);

    @Named("mapRole")
    default String mapRole(Role role) {
        if (role == null) return null;
        return role.getName().name();
    }

    List<UserResponse> toResponseList(List<User> users);
}
