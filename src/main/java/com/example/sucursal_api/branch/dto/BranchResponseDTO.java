package com.example.sucursal_api.branch.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record BranchResponseDTO(
        UUID id,
        String name,
        String slug,
        String address,
        String primaryPhone,
        BigDecimal lat,
        BigDecimal lng,
        boolean active
) {}

