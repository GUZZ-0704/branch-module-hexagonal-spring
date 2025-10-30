package com.example.sucursal_api.config;


import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DevTools {

    @Bean
    @ConditionalOnProperty(name = "app.print-hash", havingValue = "true")
    CommandLineRunner printAdminHash(PasswordEncoder encoder) {
        return args -> {
            String raw = "Admin123!"; // <- TU contraseña en texto plano
            System.out.println("\n================ BCrypt ================");
            System.out.println("Raw: " + raw);
            System.out.println("Hash: " + encoder.encode(raw));
            System.out.println("=======================================\n");
        };
    }
}

