package com.example.sucursal_api.chatbot.domain;

import java.util.UUID;

public record ProductStockInfo(
        UUID productId,
        String productName,
        int totalStock
) {}
