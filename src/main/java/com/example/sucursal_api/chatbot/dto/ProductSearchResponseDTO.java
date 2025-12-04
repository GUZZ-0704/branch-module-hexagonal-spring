package com.example.sucursal_api.chatbot.dto;

import java.util.List;

public record ProductSearchResponseDTO(
        String query,
        int totalProductsFound,
        int branchesSearched,
        List<BranchProductsDTO> results
) {}
