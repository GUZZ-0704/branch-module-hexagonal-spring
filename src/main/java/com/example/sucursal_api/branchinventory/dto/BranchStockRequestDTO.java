package com.example.sucursal_api.branchinventory.dto;

import java.util.UUID;

public record BranchStockRequestDTO(
        UUID branchId,
        UUID productId,
        UUID batchId,
        int quantity,
        int minimumStock
) {}
