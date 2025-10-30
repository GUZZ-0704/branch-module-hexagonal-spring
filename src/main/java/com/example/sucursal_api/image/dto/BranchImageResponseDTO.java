package com.example.sucursal_api.image.dto;


import java.util.UUID;

public record BranchImageResponseDTO(
        UUID id,
        UUID branchId,
        String url,
        String title,
        String altText,
        boolean cover
) {}

