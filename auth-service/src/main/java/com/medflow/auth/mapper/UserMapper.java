package com.medflow.auth.mapper;

import com.medflow.auth.dto.response.UserResponse;
import com.medflow.auth.entity.Role;
import com.medflow.auth.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "roles", source = "roles", qualifiedByName = "mapRoles")
    UserResponse toResponse(User user);

    @Named("mapRoles")
    default List<String> mapRoles(Set<Role> roles) {
        return roles.stream()
                .map(role -> role.getName().name())
                .collect(Collectors.toList());
    }

    List<UserResponse> toResponseList(List<User> users);
}
