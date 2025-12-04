package com.example.sucursal_api.chatbot.domain;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductInfo(
        UUID id,
        String name,
        String sku,
        String brand,
        String category,
        BigDecimal unitPrice
) {}
