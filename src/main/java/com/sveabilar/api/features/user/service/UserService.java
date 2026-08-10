package com.sveabilar.api.features.user.service;


import com.sveabilar.api.features.user.dto.UserResponse;

public interface UserService {

    UserResponse getUserByEmail(String email);
}