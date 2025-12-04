package com.example.sucursal_api.chatbot.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record ProductResponseDTO(
        UUID id,
        String name,
        String description,
        String sku,
        String brand,
        String category,
        BigDecimal unitPrice,
        String unit,
        boolean active,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
