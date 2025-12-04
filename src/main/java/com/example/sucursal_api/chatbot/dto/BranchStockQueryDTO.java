package com.example.sucursal_api.chatbot.dto;

import java.util.UUID;

public record BranchStockQueryDTO(
        UUID branchId,
        String sku
) {}
