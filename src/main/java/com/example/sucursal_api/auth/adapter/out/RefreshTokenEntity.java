package com.example.sucursal_api.auth.adapter.out;


import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "refresh_token",
        indexes = @Index(name = "ix_refresh_token_token", columnList = "token", unique = true))
public class RefreshTokenEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_account_id", nullable = false)
    private UserAccountEntity userAccount;

    @Column(name = "token", nullable = false, length = 200)
    private String token;

    @Column(name = "expires_at", nullable = false)
    private Instant expiresAt;

    @Column(name = "revoked", nullable = false)
    private boolean revoked = false;

    // getters/setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UserAccountEntity getUserAccount() { return userAccount; }
    public void setUserAccount(UserAccountEntity userAccount) { this.userAccount = userAccount; }
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    public Instant getExpiresAt() { return expiresAt; }
    public void setExpiresAt(Instant expiresAt) { this.expiresAt = expiresAt; }
    public boolean isRevoked() { return revoked; }
    public void setRevoked(boolean revoked) { this.revoked = revoked; }
}
