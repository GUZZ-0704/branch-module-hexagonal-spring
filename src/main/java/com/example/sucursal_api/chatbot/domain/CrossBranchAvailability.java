package com.example.sucursal_api.chatbot.domain;

import java.util.UUID;

public record CrossBranchAvailability(
        UUID productId,
        String productName,
        UUID branchId,
        String branchName,
        int availableStock
) {}
