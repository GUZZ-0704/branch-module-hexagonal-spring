package com.example.sucursal_api.chatbot.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record StockMovementResponseDTO(
        UUID id,
        UUID productId,
        String productName,
        String productBrand,
        UUID batchId,
        String batchNumber,
        UUID sourceBranchId,
        String sourceBranchName,
        UUID destinationBranchId,
        String destinationBranchName,
        int quantity,
        String movementType,
        String reason,
        String performedBy,
        LocalDateTime createdAt
) {}