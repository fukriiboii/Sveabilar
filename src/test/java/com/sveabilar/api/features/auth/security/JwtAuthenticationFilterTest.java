package com.sveabilar.api.features.auth.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import jakarta.servlet.FilterChain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("JwtAuthenticationFilter Unit Tests")
class JwtAuthenticationFilterTest {

    @Mock
    private JwtServiceImpl jwtService;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private JwtAuthenticationFilter filter;

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    // ─────────────────────────────────────────────
    // NO AUTHORIZATION HEADER
    // ─────────────────────────────────────────────

    @Nested
    @DisplayName("When Authorization header is missing or malformed")
    class NoAuthHeaderTests {

        @Test
        @DisplayName("Should pass through when Authorization header is missing")
        void shouldPassThroughWhenNoAuthHeader() throws Exception {

            // GIVEN
            when(request.getHeader("Authorization")).thenReturn(null);

            // WHEN
            filter.doFilterInternal(request, response, filterChain);

            // THEN
            verify(filterChain).doFilter(request, response);

            assertThat(SecurityContextHolder.getContext().getAuthentication())
                    .isNull();
        }

        @Test
        @DisplayName("Should pass through when Authorization header does not start with Bearer")
        void shouldPassThroughWhenNotBearerToken() throws Exception {

            // GIVEN
            when(request.getHeader("Authorization"))
                    .thenReturn("Basic somebase64");

            // WHEN
            filter.doFilterInternal(request, response, filterChain);

            // THEN
            verify(filterChain).doFilter(request, response);

            assertThat(SecurityContextHolder.getContext().getAuthentication())
                    .isNull();
        }
    }

    // ─────────────────────────────────────────────
    // VALID TOKEN
    // ─────────────────────────────────────────────

    @Nested
    @DisplayName("When Authorization header contains a valid token")
    class ValidTokenTests {

        @Test
        @DisplayName("Should set authentication when token is valid")
        void shouldSetAuthenticationWhenTokenIsValid() throws Exception {

            // GIVEN
            UserDetails userDetails = User
                    .withUsername("admin@sveabilar.se")
                    .password("irrelevant")
                    .roles("ADMIN")
                    .build();

            when(request.getHeader("Authorization"))
                    .thenReturn("Bearer valid-token");

            when(jwtService.extractEmail("valid-token"))
                    .thenReturn("admin@sveabilar.se");

            when(userDetailsService.loadUserByUsername("admin@sveabilar.se"))
                    .thenReturn(userDetails);

            when(jwtService.isTokenValid("valid-token", userDetails))
                    .thenReturn(true);

            // WHEN
            filter.doFilterInternal(request, response, filterChain);

            // THEN
            assertThat(SecurityContextHolder.getContext().getAuthentication())
                    .isNotNull();

            assertThat(SecurityContextHolder.getContext().getAuthentication().getName())
                    .isEqualTo("admin@sveabilar.se");

            assertThat(SecurityContextHolder.getContext().getAuthentication().getAuthorities())
                    .extracting("authority")
                    .containsExactly("ROLE_ADMIN");

            verify(filterChain).doFilter(request, response);
        }
    }

    // ─────────────────────────────────────────────
    // INVALID TOKEN
    // ─────────────────────────────────────────────

    @Nested
    @DisplayName("When Authorization header contains an invalid token")
    class InvalidTokenTests {

        @Test
        @DisplayName("Should not set authentication when token is invalid")
        void shouldNotSetAuthenticationWhenTokenIsInvalid() throws Exception {

            // GIVEN
            UserDetails userDetails = User
                    .withUsername("admin@sveabilar.se")
                    .password("irrelevant")
                    .roles("ADMIN")
                    .build();

            when(request.getHeader("Authorization"))
                    .thenReturn("Bearer invalid-token");

            when(jwtService.extractEmail("invalid-token"))
                    .thenReturn("admin@sveabilar.se");

            when(userDetailsService.loadUserByUsername("admin@sveabilar.se"))
                    .thenReturn(userDetails);

            when(jwtService.isTokenValid("invalid-token", userDetails))
                    .thenReturn(false);

            // WHEN
            filter.doFilterInternal(request, response, filterChain);

            // THEN
            assertThat(SecurityContextHolder.getContext().getAuthentication())
                    .isNull();

            verify(filterChain).doFilter(request, response);
        }

        @Test
        @DisplayName("Should not authenticate when token does not contain an email")
        void shouldNotAuthenticateWhenEmailIsMissing() throws Exception {

            // GIVEN
            when(request.getHeader("Authorization"))
                    .thenReturn("Bearer token-without-email");

            when(jwtService.extractEmail("token-without-email"))
                    .thenReturn(null);

            // WHEN
            filter.doFilterInternal(request, response, filterChain);

            // THEN
            assertThat(SecurityContextHolder.getContext().getAuthentication())
                    .isNull();

            verify(filterChain).doFilter(request, response);
        }
    }

    // ─────────────────────────────────────────────
    // EXISTING AUTHENTICATION
    // ─────────────────────────────────────────────

    @Nested
    @DisplayName("When authentication already exists")
    class ExistingAuthenticationTests {

        @Test
        @DisplayName("Should not replace existing authentication")
        void shouldNotReplaceExistingAuthentication() throws Exception {

            // GIVEN
            UsernamePasswordAuthenticationToken existingAuthentication =
                    new UsernamePasswordAuthenticationToken(
                            "existing-user",
                            null
                    );

            SecurityContextHolder.getContext()
                    .setAuthentication(existingAuthentication);

            when(request.getHeader("Authorization"))
                    .thenReturn("Bearer valid-token");

            // WHEN
            filter.doFilterInternal(request, response, filterChain);

            // THEN
            assertThat(SecurityContextHolder.getContext().getAuthentication())
                    .isSameAs(existingAuthentication);

            verify(filterChain).doFilter(request, response);
        }
    }
}
