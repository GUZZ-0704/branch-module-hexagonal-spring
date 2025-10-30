package com.example.sucursal_api.auth.adapter.out;


import com.example.sucursal_api.auth.domain.Role;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(
        name = "user_account",
        uniqueConstraints = @UniqueConstraint(name = "uq_user_account_username", columnNames = "username"),
        indexes = @Index(name = "ix_user_account_employee", columnList = "employee_id")
)
public class UserAccountEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "employee_id", nullable = false)
    private UUID employeeId;

    @Column(name = "username", nullable = false, length = 255)
    private String username;

    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;

    @Column(name = "enabled", nullable = false)
    private boolean enabled = true;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_account_roles", joinColumns = @JoinColumn(name = "user_account_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, length = 32)
    private Set<Role> roles;

    @Column(name = "last_login_at")
    private LocalDateTime lastLoginAt;

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
