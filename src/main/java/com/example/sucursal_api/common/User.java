package com.example.sucursal_api.common;


import java.util.Set;
import java.util.UUID;

public class User {
    private UUID id;
    private UUID employeeId;
    private String username;
    private Set<String> roles;

    public User() {}

    public User(UUID id, UUID employeeId, String username, Set<String> roles) {
        this.id = id;
        this.employeeId = employeeId;
        this.username = username;
        this.roles = roles;
    }

    public UUID getId() { return id; }
    public UUID getEmployeeId() { return employeeId; }
    public String getUsername() { return username; }
    public Set<String> getRoles() { return roles; }

    public boolean hasRole(String role) {
        return roles != null && roles.stream().anyMatch(r -> r.equalsIgnoreCase(role));
    }
}
