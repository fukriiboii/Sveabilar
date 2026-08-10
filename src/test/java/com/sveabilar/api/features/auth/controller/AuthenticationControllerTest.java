package com.sveabilar.api.features.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sveabilar.api.common.exception.GlobalExceptionHandler;
import com.sveabilar.api.features.auth.dto.LoginRequest;
import com.sveabilar.api.features.auth.dto.LoginResponse;
import com.sveabilar.api.features.auth.exception.InvalidCredentialsException;
import com.sveabilar.api.features.auth.service.AuthenticationService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthenticationController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(GlobalExceptionHandler.class)
@DisplayName("AuthenticationController Unit Tests")
class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private AuthenticationService authenticationService;

    @Nested
    @DisplayName("POST /api/auth/login")
    class Login {

        @Test
        @DisplayName("Should return 200 OK with access token when credentials are valid")
        void shouldReturn200WithAccessTokenWhenCredentialsAreValid() throws Exception {

            // GIVEN
            LoginRequest request = new LoginRequest();
            request.setEmail("admin@sveabilar.se");
            request.setPassword("admin123");

            LoginResponse response = new LoginResponse("jwt-token");

            when(authenticationService.login(any(LoginRequest.class)))
                    .thenReturn(response);

            // WHEN + THEN
            mockMvc.perform(
                    post("/api/auth/login")
                            .contentType("application/json")
                            .content(objectMapper.writeValueAsString(request))
            )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.accessToken").value("jwt-token"));

            verify(authenticationService).login(any(LoginRequest.class));
        }

        @Test
        @DisplayName("Should return 401 Unauthorized when credentials are invalid")
        void shouldReturn401WhenCredentialsAreInvalid() throws Exception {

            // GIVEN
            LoginRequest request = new LoginRequest();
            request.setEmail("admin@sveabilar.se");
            request.setPassword("wrong-password");

            when(authenticationService.login(any(LoginRequest.class)))
                    .thenThrow(new InvalidCredentialsException("Invalid email or password"));

            // WHEN + THEN
            mockMvc.perform(
                    post("/api/auth/login")
                            .contentType("application/json")
                            .content(objectMapper.writeValueAsString(request))
            )
                    .andExpect(status().isUnauthorized());
        }
    }
}