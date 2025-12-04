package com.example.sucursal_api.chatbot.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record BranchStockResponseDTO(
        UUID id,
        UUID branchId,
        String branchName,
        UUID productId,
        String productName,
        String productBrand,
        UUID batchId,
        String batchNumber,
        int quantity,
        int minimumStock,
        boolean lowStock,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
