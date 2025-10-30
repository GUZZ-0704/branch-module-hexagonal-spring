package com.example.sucursal_api.auth.domain;


import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

public class UserAccount {
    private UUID id;
    private UUID employeeId;
    private String username;
    private String passwordHash;
    private boolean enabled;
    private Set<Role> roles;
    private LocalDateTime lastLoginAt;

    public UserAccount() {}

    public UserAccount(UUID id, UUID employeeId, String username, String passwordHash,
                       boolean enabled, Set<Role> roles, LocalDateTime lastLoginAt) {
        this.id = id;
        this.employeeId = employeeId;
        this.username = username;
        this.passwordHash = passwordHash;
        this.enabled = enabled;
        this.roles = roles;
        this.lastLoginAt = lastLoginAt;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public UUID getEmployeeId() { return employeeId; }
    public void setEmployeeId(UUID employeeId) { this.employeeId = employeeId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }

    public Set<Role> getRoles() { return roles; }
    public void setRoles(Set<Role> roles) { this.roles = roles; }

    public LocalDateTime getLastLoginAt() { return lastLoginAt; }
    public void setLastLoginAt(LocalDateTime lastLoginAt) { this.lastLoginAt = lastLoginAt; }
}
