package com.example.sucursal_api.auth.dto;


import java.util.Set;

public record AuthResponseDTO(
        String accessToken,
        String tokenType,
        long expiresIn,
        String refreshToken,
        Set<String> roles
) {}

