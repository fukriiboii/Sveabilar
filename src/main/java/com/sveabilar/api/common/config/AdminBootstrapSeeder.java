package com.sveabilar.api.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.sveabilar.api.features.user.entity.Role;
import com.sveabilar.api.features.user.entity.User;
import com.sveabilar.api.features.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Configuration
@Profile("prod")
@RequiredArgsConstructor
public class AdminBootstrapSeeder {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${ADMIN_BOOTSTRAP_ENABLED:false}")
    private boolean bootstrapEnabled;

    @Value("${ADMIN_EMAIL:}")
    private String adminEmail;

    @Value("${ADMIN_PASSWORD:}")
    private String adminPassword;

    @Bean
    CommandLineRunner bootstrapAdmin() {
        return args -> {
            if (!bootstrapEnabled) {
                return;
            }

            validateConfiguration();

            User admin = userRepository.findByEmail(adminEmail).orElseGet(User::new);

            if (admin.getId() != null
                    && passwordEncoder.matches(adminPassword, admin.getPassword())
                    && admin.getRole() == Role.ADMIN
                    && admin.isActive()) {
                return;
            }

            admin.setEmail(adminEmail);
            admin.setPassword(passwordEncoder.encode(adminPassword));
            admin.setRole(Role.ADMIN);
            admin.setActive(true);

            userRepository.save(admin);
        };
    }

    private void validateConfiguration() {
        if (adminEmail.isBlank() || adminPassword.isBlank()) {
            throw new IllegalStateException(
                    "ADMIN_EMAIL and ADMIN_PASSWORD must be set when admin bootstrap is enabled");
        }
    }
}