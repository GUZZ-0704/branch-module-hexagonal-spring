package com.example.sucursal_api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

@Configuration
public class AdminSeeder {

    @Bean
    @ConditionalOnProperty(name = "app.seed.admin", havingValue = "true")
    CommandLineRunner seedAdminUser(JdbcTemplate jdbc, PasswordEncoder encoder,
                                    @Value("${app.seed.admin.email:admin@empresa.com}") String email,
                                    @Value("${app.seed.admin.password:Admin123!}") String rawPassword,
                                    @Value("${app.seed.admin.firstName:Admin}") String firstName,
                                    @Value("${app.seed.admin.lastName:Root}") String lastName) {
        return args -> {
            Integer cnt = jdbc.queryForObject(
                    "select count(1) from user_account where username = ?",
                    Integer.class, email
            );
            if (cnt != null && cnt > 0) {
                System.out.println("[Seeder] Admin ya existe: " + email);
                return;
            }

            UUID employeeId = UUID.randomUUID();
            jdbc.update("""
                insert into employee (
                  id, first_name, last_name, doc_type, doc_number,
                  personal_email, institutional_email, status
                ) values (?,?,?,?,?,?,?,?)
                """,
                    employeeId, firstName, lastName, "CI", "0",
                    email, email, "ACTIVE"
            );

            UUID accountId = UUID.randomUUID();
            String hash = encoder.encode(rawPassword);
            jdbc.update("""
                insert into user_account (
                  id, employee_id, username, password_hash, enabled, last_login_at
                ) values (?,?,?,?,?, null)
                """,
                    accountId, employeeId, email, hash, true
            );

            jdbc.update("""
                insert into user_account_roles (user_account_id, role)
                values (?, ?)
                """, accountId, "ADMIN");

            System.out.println("[Seeder] Admin creado: " + email + " / rol=ADMIN");
        };
    }
}

