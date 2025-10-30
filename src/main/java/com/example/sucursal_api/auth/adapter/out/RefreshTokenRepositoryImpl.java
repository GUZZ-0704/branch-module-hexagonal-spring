package com.example.sucursal_api.auth.adapter.out;


import com.example.sucursal_api.auth.domain.RefreshToken;
import com.example.sucursal_api.auth.port.out.RefreshTokenRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Repository
public class RefreshTokenRepositoryImpl implements RefreshTokenRepository {

    private final RefreshTokenJpaRepository jpa;
    @PersistenceContext
    private final EntityManager em;

    public RefreshTokenRepositoryImpl(RefreshTokenJpaRepository jpa, EntityManager em) {
        this.jpa = jpa;
        this.em = em;
    }

    private RefreshToken toDomain(RefreshTokenEntity e) {
        return new RefreshToken(e.getId(), e.getUserAccount().getId(),
                e.getToken(), e.getExpiresAt(), e.isRevoked());
    }

    private RefreshTokenEntity toEntity(RefreshToken d) {
        RefreshTokenEntity e = new RefreshTokenEntity();
        if (d.getId() != null) e.setId(d.getId());
        e.setUserAccount(em.getReference(UserAccountEntity.class, d.getUserAccountId()));
        e.setToken(d.getToken());
        e.setExpiresAt(d.getExpiresAt());
        e.setRevoked(d.isRevoked());
        return e;
    }

    @Override
    public RefreshToken save(RefreshToken token) {
        RefreshTokenEntity saved = jpa.save(toEntity(token));
        return toDomain(saved);
    }

    @Override
    public Optional<RefreshToken> findValidByToken(String token) {
        return jpa.findByTokenAndRevokedFalseAndExpiresAtAfter(token, Instant.now())
                .map(this::toDomain);
    }

    @Override
    public void revokeById(UUID id) {
        var e = jpa.findById(id).orElseThrow(() -> new java.util.NoSuchElementException("RefreshToken no encontrado"));
        e.setRevoked(true);
        jpa.save(e);
    }
}

