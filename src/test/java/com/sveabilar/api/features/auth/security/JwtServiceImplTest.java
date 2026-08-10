package com.sveabilar.api.features.auth.security;

import com.sveabilar.api.features.user.entity.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("JwtServiceImpl Unit Tests")
class JwtServiceImplTest {

    private JwtServiceImpl jwtService;

    private static final String SECRET = "4e7638792f423f4428472b4b6250655368566d597133743677397a2443264629";
    private static final long EXPIRATION = 86400000L;
    private static final long EXPIRED = -1000L; // already expired

    @BeforeEach
    void setUp() {
        jwtService = new JwtServiceImpl(SECRET, EXPIRATION);
    }

    @Nested
    @DisplayName("generateAccessToken()")
    class GenerateAccessTokenTests {

        @Test
        @DisplayName("Should generate valid token")
        void shouldGenerateValidToken() {
            String token = jwtService.generateAccessToken(1L, "admin@sveabilar.se", Role.ADMIN);

            assertThat(token).isNotNull();
            assertThat(token.split("\\.")).hasSize(3); // JWT har tre delar
        }
    }

    @Nested
    @DisplayName("extractEmail()")
    class ExtractEmailTests {

        @Test
        @DisplayName("Should extract email from token")
        void shouldExtractEmailFromToken() {
            String token = jwtService.generateAccessToken(1L, "admin@sveabilar.se", Role.ADMIN);

            String email = jwtService.extractEmail(token);

            assertThat(email).isEqualTo("admin@sveabilar.se");
        }
    }

    @Nested
    @DisplayName("isTokenValid()")
    class IsTokenValidTests {

        @Test
        @DisplayName("Should validate valid token")
        void shouldValidateValidToken() {
            String token = jwtService.generateAccessToken(1L, "admin@sveabilar.se", Role.ADMIN);
            UserDetails userDetails = User.withUsername("admin@sveabilar.se")
                    .password("irrelevant")
                    .roles("ADMIN")
                    .build();

            boolean valid = jwtService.isTokenValid(token, userDetails);

            assertThat(valid).isTrue();
        }

        @Test
        @DisplayName("Should reject token with wrong email")
        void shouldRejectInvalidToken() {
            String token = jwtService.generateAccessToken(1L, "admin@sveabilar.se", Role.ADMIN);
            UserDetails otherUser = User.withUsername("other@sveabilar.se")
                    .password("irrelevant")
                    .roles("ADMIN")
                    .build();

            boolean valid = jwtService.isTokenValid(token, otherUser);

            assertThat(valid).isFalse();
        }

        @Test
        @DisplayName("Should reject expired token")
        void shouldRejectExpiredToken() {
            JwtServiceImpl expiredJwtService = new JwtServiceImpl(SECRET, EXPIRED);
            String token = expiredJwtService.generateAccessToken(1L, "admin@sveabilar.se", Role.ADMIN);
            UserDetails userDetails = User.withUsername("admin@sveabilar.se")
                    .password("irrelevant")
                    .roles("ADMIN")
                    .build();

            assertThatThrownBy(() -> expiredJwtService.isTokenValid(token, userDetails))
                    .isInstanceOf(io.jsonwebtoken.ExpiredJwtException.class);
        }
    }
}