package com.example.sucursal_api.auth.dto;


import jakarta.validation.constraints.NotBlank;

public record LoginRequestDTO(
        @NotBlank String username,
        @NotBlank String password
) {}

