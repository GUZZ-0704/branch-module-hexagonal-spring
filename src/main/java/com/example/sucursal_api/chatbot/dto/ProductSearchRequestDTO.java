package com.example.sucursal_api.chatbot.dto;

import java.util.UUID;

public record ProductSearchRequestDTO(
        String productName,
        String category,
        String brand,
        UUID branchId,
        boolean searchAllBranches
) {
    public ProductSearchRequestDTO {
        if (productName == null && category == null && brand == null) {
            throw new IllegalArgumentException("Debe proporcionar al menos un criterio de búsqueda");
        }
    }
}
