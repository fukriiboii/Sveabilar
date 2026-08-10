package com.sveabilar.api.features.auth.service;

import com.sveabilar.api.features.auth.dto.LoginRequest;
import com.sveabilar.api.features.auth.dto.LoginResponse;
import com.sveabilar.api.features.auth.exception.InvalidCredentialsException;
import com.sveabilar.api.features.auth.exception.UserInactiveException;
import com.sveabilar.api.features.auth.security.JwtService;
import com.sveabilar.api.features.user.entity.Role;
import com.sveabilar.api.features.user.entity.User;
import com.sveabilar.api.features.user.repository.UserRepository;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuthenticationServiceImpl Unit Tests")
class AuthenticationServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    @Nested
    @DisplayName("login()")
    class LoginTests {

        @Test
        @DisplayName("Should return token response when credentials are valid")
        void shouldReturnTokenWhenCredentialsAreValid() {

            // GIVEN
            LoginRequest request = new LoginRequest();
            request.setEmail("admin@sveabilar.se");
            request.setPassword("admin123");

            User user = new User();
            user.setId(1L);
            user.setEmail("admin@sveabilar.se");
            user.setRole(Role.ADMIN);
            user.setActive(true);

            when(authenticationManager.authenticate(
                    any(UsernamePasswordAuthenticationToken.class)))
                    .thenReturn(null);

            when(userRepository.findByEmail("admin@sveabilar.se"))
                    .thenReturn(Optional.of(user));

            when(jwtService.generateAccessToken(
                    1L,
                    "admin@sveabilar.se",
                    Role.ADMIN))
                    .thenReturn("jwt-token");

            // WHEN
            LoginResponse response = authenticationService.login(request);

            // THEN
            assertThat(response).isNotNull();
            assertThat(response.getAccessToken()).isEqualTo("jwt-token");
        }

        @Test
        @DisplayName("Should throw InvalidCredentialsException when authentication fails")
        void shouldThrowWhenAuthenticationFails() {

            // GIVEN
            LoginRequest request = new LoginRequest();
            request.setEmail("admin@sveabilar.se");
            request.setPassword("wrong-password");

            when(authenticationManager.authenticate(
                    any(UsernamePasswordAuthenticationToken.class)))
                    .thenThrow(new BadCredentialsException("Bad credentials"));

            // WHEN + THEN
            assertThatThrownBy(() -> authenticationService.login(request))
                    .isInstanceOf(InvalidCredentialsException.class)
                    .hasMessage("Ogiltig E-post eller lösenord");

            verify(userRepository, never()).findByEmail(any());
            verify(jwtService, never()).generateAccessToken(any(), any(), any());
        }

        @Test
        @DisplayName("Should throw InvalidCredentialsException when user is not found")
        void shouldThrowWhenUserIsNotFound() {

            // GIVEN
            LoginRequest request = new LoginRequest();
            request.setEmail("admin@sveabilar.se");
            request.setPassword("admin123");

            when(authenticationManager.authenticate(
                    any(UsernamePasswordAuthenticationToken.class)))
                    .thenReturn(null);

            when(userRepository.findByEmail("admin@sveabilar.se"))
                    .thenReturn(Optional.empty());

            // WHEN + THEN
            assertThatThrownBy(() -> authenticationService.login(request))
                    .isInstanceOf(InvalidCredentialsException.class)
                    .hasMessage("Ogiltig E-post eller lösenord");

            verify(jwtService, never())
                    .generateAccessToken(any(), any(), any());
        }

        @Test
        @DisplayName("Should generate token using authenticated user data")
        void shouldGenerateTokenUsingUserData() {

            // GIVEN
            LoginRequest request = new LoginRequest();
            request.setEmail("admin@sveabilar.se");
            request.setPassword("admin123");

            User user = new User();
            user.setId(1L);
            user.setEmail("admin@sveabilar.se");
            user.setRole(Role.ADMIN);
            user.setActive(true);

            when(authenticationManager.authenticate(
                    any(UsernamePasswordAuthenticationToken.class)))
                    .thenReturn(null);

            when(userRepository.findByEmail("admin@sveabilar.se"))
                    .thenReturn(Optional.of(user));

            when(jwtService.generateAccessToken(
                    1L,
                    "admin@sveabilar.se",
                    Role.ADMIN))
                    .thenReturn("jwt-token");

            // WHEN
            authenticationService.login(request);

            // THEN
            verify(jwtService).generateAccessToken(
                    1L,
                    "admin@sveabilar.se",
                    Role.ADMIN);
        }

        @Test
        @DisplayName("Should throw InvalidCredentialsException when user is inactive")
        void shouldThrowWhenUserIsInactive() {

            // GIVEN
            LoginRequest request = new LoginRequest();
            request.setEmail("admin@sveabilar.se");
            request.setPassword("admin123");

            User user = new User();
            user.setId(1L);
            user.setEmail("admin@sveabilar.se");
            user.setRole(Role.ADMIN);
            user.setActive(false);

            when(authenticationManager.authenticate(
                    any(UsernamePasswordAuthenticationToken.class)))
                    .thenReturn(null);

            when(userRepository.findByEmail("admin@sveabilar.se"))
                    .thenReturn(Optional.of(user));

            // WHEN + THEN
            assertThatThrownBy(() -> authenticationService.login(request))
                    .isInstanceOf(UserInactiveException.class)
                    .hasMessage("Användaren är inaktiv");

            verify(jwtService, never())
                    .generateAccessToken(any(), any(), any());
        }

        @Test
        @DisplayName("Should authenticate using provided email and password")
        void shouldAuthenticateUsingProvidedCredentials() {

            // GIVEN
            LoginRequest request = new LoginRequest();
            request.setEmail("admin@sveabilar.se");
            request.setPassword("admin123");

            User user = new User();
            user.setId(1L);
            user.setEmail("admin@sveabilar.se");
            user.setRole(Role.ADMIN);
            user.setActive(true);

            when(authenticationManager.authenticate(
                    any(UsernamePasswordAuthenticationToken.class)))
                    .thenReturn(null);

            when(userRepository.findByEmail("admin@sveabilar.se"))
                    .thenReturn(Optional.of(user));

            when(jwtService.generateAccessToken(
                    1L,
                    "admin@sveabilar.se",
                    Role.ADMIN))
                    .thenReturn("jwt-token");

            // WHEN
            authenticationService.login(request);

            // THEN
            verify(authenticationManager).authenticate(
                    new UsernamePasswordAuthenticationToken(
                            "admin@sveabilar.se",
                            "admin123"));
        }
    }
}