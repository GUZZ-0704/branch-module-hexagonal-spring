package com.example.sucursal_api.chatbot.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductInfoDTO(
        UUID productId,
        String name,
        String description,
        String sku,
        String brand,
        String category,
        BigDecimal unitPrice,
        String unit,
        int availableQuantity
) {}
