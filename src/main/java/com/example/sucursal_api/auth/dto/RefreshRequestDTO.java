package com.example.sucursal_api.auth.dto;


import jakarta.validation.constraints.NotBlank;

public record RefreshRequestDTO(
        @NotBlank String refreshToken
) {}
