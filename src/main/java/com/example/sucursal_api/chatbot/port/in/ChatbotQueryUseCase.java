package com.example.sucursal_api.chatbot.port.in;

import com.example.sucursal_api.chatbot.dto.*;

import java.util.UUID;

public interface ChatbotQueryUseCase {
    
    /**
     * Busca productos en una sucursal específica o en todas las sucursales activas
     */
    ProductSearchResponseDTO searchProducts(ProductSearchRequestDTO request);
    
    /**
     * Obtiene la lista de sucursales activas
     */
    BranchListResponseDTO getActiveBranches();
    
    /**
     * Obtiene información de una sucursal específica
     */
    BranchInfoDTO getBranchInfo(UUID branchId);
    
    /**
     * Obtiene todos los productos de una sucursal
     */
    BranchProductsDTO getProductsByBranch(UUID branchId);
    
    /**
     * Busca un producto en todas las sucursales y devuelve dónde está disponible
     */
    ProductSearchResponseDTO findProductAvailability(String productName);
}
