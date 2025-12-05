package com.example.sucursal_api.branchinventory.dto;

import java.util.UUID;

public record BranchStockTransferRequestDTO(
        UUID sourceStockId,
        UUID targetBranchId,
        int quantity
) {}
