package com.example.sucursal_api.auth.adapter.out;


import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserAccountJpaRepository extends JpaRepository<UserAccountEntity, UUID> {
    Optional<UserAccountEntity> findByUsername(String username);
    boolean existsByUsername(String username);
}
