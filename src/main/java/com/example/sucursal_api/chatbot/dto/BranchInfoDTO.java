package com.example.sucursal_api.chatbot.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record BranchInfoDTO(
        UUID id,
        String name,
        String slug,
        String address,
        String primaryPhone,
        BigDecimal lat,
        BigDecimal lng,
        boolean active
) {}
