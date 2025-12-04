package com.example.sucursal_api.chatbot.dto;

import java.util.List;
import java.util.UUID;

public record BranchProductsDTO(
        UUID branchId,
        String branchName,
        String branchSlug,
        String address,
        boolean active,
        List<ProductInfoDTO> products
) {}
