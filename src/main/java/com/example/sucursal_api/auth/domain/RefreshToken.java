package com.example.sucursal_api.auth.domain;


import java.time.Instant;
import java.util.UUID;

public class RefreshToken {
    private UUID id;
    private UUID userAccountId;
    private String token;
    private Instant expiresAt;
    private boolean revoked;

    public RefreshToken() {}

    public RefreshToken(UUID id, UUID userAccountId, String token, Instant expiresAt, boolean revoked) {
        this.id = id;
        this.userAccountId = userAccountId;
        this.token = token;
        this.expiresAt = expiresAt;
        this.revoked = revoked;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getUserAccountId() { return userAccountId; }
    public void setUserAccountId(UUID userAccountId) { this.userAccountId = userAccountId; }
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    public Instant getExpiresAt() { return expiresAt; }
    public void setExpiresAt(Instant expiresAt) { this.expiresAt = expiresAt; }
    public boolean isRevoked() { return revoked; }
    public void setRevoked(boolean revoked) { this.revoked = revoked; }
}
