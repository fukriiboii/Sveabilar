package com.sveabilar.api.common.config;

import com.sveabilar.api.features.user.entity.Role;
import com.sveabilar.api.features.user.entity.User;
import com.sveabilar.api.features.user.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@Profile("!prod")
public class LocalAdminSeeder {

    @Bean
    CommandLineRunner seedLocalAdmin(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            String email = "admin@sveabilar.se";

            if (userRepository.findByEmail(email).isPresent()) {
                return;
            }

            User admin = new User();
            admin.setEmail(email);
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole(Role.ADMIN);
            admin.setActive(true);

            userRepository.save(admin);
        };
    }
}