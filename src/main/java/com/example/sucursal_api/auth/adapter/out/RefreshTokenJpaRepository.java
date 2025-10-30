package com.example.sucursal_api.auth.adapter.out;


import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenJpaRepository extends JpaRepository<RefreshTokenEntity, UUID> {
    Optional<RefreshTokenEntity> findByTokenAndRevokedFalseAndExpiresAtAfter(String token, Instant now);
}

