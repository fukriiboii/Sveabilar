package com.sveabilar.api.features.auth.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import com.sveabilar.api.features.auth.dto.LoginRequest;
import com.sveabilar.api.features.auth.dto.LoginResponse;
import com.sveabilar.api.features.auth.exception.InvalidCredentialsException;
import com.sveabilar.api.features.auth.exception.UserInactiveException;
import com.sveabilar.api.features.auth.security.JwtService;
import com.sveabilar.api.features.user.entity.User;
import com.sveabilar.api.features.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthenticationManager authenticationManager; 
    private final UserRepository userRepository; 
    private final JwtService jwtService; 

    @Override
    public LoginResponse login(LoginRequest request) {
        try {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    request.getEmail(), 
                    request.getPassword()
                )
            );
        } catch (BadCredentialsException e) {
            throw new InvalidCredentialsException(
                "Ogiltig E-post eller lösenord"
            );
        }

        User user = userRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new InvalidCredentialsException("Ogiltig E-post eller lösenord")
        );

        if (!user.isActive()) {
            throw new UserInactiveException("Användaren är inaktiv");
        }

        String accessToken = jwtService.generateAccessToken(
            user.getId(), 
            user.getEmail(), 
            user.getRole()
        );
        
        return new LoginResponse(accessToken); 
    }
    
}
