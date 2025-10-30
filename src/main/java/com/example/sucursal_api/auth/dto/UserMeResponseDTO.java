package com.example.sucursal_api.auth.dto;


import java.util.Set;
import java.util.UUID;

public record UserMeResponseDTO(
        UUID userId,
        UUID employeeId,
        String username,
        String fullName,
        Set<String> roles
) {}
