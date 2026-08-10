package com.sveabilar.api.features.auth.security;

import org.springframework.security.core.userdetails.UserDetails;

import com.sveabilar.api.features.user.entity.Role;

public interface JwtService {

    String generateAccessToken(Long userId, String email, Role role);
    String extractEmail(String token); 
    boolean isTokenValid(String token, UserDetails userDetails); 
    boolean isTokenExpired(String token); 
}