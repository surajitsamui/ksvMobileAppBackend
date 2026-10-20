 package com.example.demo.config;

import com.example.demo.model.AppUser;
import com.example.demo.model.Devotee;
import com.example.demo.model.Role;
import com.example.demo.repository.DevoteeRepository;
import com.example.demo.repository.UserRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner seedDemoData(
            UserRepository users,
            DevoteeRepository devotees,
            PasswordEncoder encoder) {

        return args -> {

            // DEVELOPMENT ONLY: remove before production deployment.
            if (users.findByEmailIgnoreCase(
                    "admin@temple.local").isEmpty()) {

                users.save(new AppUser(
                        "admin@temple.local",
                        encoder.encode("Admin@12345"),
                        Role.ADMIN,
                        "Temple Admin",
                        "9000000000",
                        null
                ));
            }

            if (users.findByEmailIgnoreCase(
                    "devotee@temple.local").isEmpty()) {

                Devotee d = devotees.findAll()
                        .stream()
                        .findFirst()
                        .orElseGet(() -> devotees.save(
                                new Devotee(
                                        "Demo Devotee",
                                        "devotee@example.com",
                                        "9000000001",
                                        "",
                                        new BigDecimal("100")
                                )
                        ));

                users.save(new AppUser(
                        "devotee@temple.local",
                        encoder.encode("Devotee@12345"),
                        Role.DEVOTEE,
                        d.getName(),
                        d.getMobile(),
                        d
                ));
            }
        };
    }
}
