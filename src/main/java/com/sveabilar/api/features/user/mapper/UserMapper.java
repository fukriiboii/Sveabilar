package com.sveabilar.api.features.user.mapper;

import org.springframework.stereotype.Component;

import com.sveabilar.api.features.user.dto.UserResponse;
import com.sveabilar.api.features.user.entity.User;

@Component
public class UserMapper {

    public UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getRole(),
                user.isActive(),
                user.getCreatedAt(),
                user.getUpdatedAt());
    }
}

