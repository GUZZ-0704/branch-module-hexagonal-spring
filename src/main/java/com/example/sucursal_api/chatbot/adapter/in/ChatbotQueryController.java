package com.example.sucursal_api.chatbot.adapter.in;

import com.example.sucursal_api.chatbot.dto.*;
import com.example.sucursal_api.chatbot.port.in.ChatbotQueryUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/chatbot-query")
public class ChatbotQueryController {

    private final ChatbotQueryUseCase useCase;

    public ChatbotQueryController(ChatbotQueryUseCase useCase) {
        this.useCase = useCase;
    }

    /**
     * Busca productos según criterios (nombre, categoría, marca)
     * Puede buscar en una sucursal específica o en todas
     */
    @PostMapping("/products/search")
    public ResponseEntity<ProductSearchResponseDTO> searchProducts(
            @RequestBody ProductSearchRequestDTO request) {
        return ResponseEntity.ok(useCase.searchProducts(request));
    }

    /**
     * Busca un producto por nombre en todas las sucursales
     * Devuelve en qué sucursales está disponible
     */
    @GetMapping("/products/availability")
    public ResponseEntity<ProductSearchResponseDTO> findProductAvailability(
            @RequestParam String productName) {
        return ResponseEntity.ok(useCase.findProductAvailability(productName));
    }

    /**
     * Obtiene la lista de sucursales activas
     */
    @GetMapping("/branches")
    public ResponseEntity<BranchListResponseDTO> getActiveBranches() {
        return ResponseEntity.ok(useCase.getActiveBranches());
    }

    /**
     * Obtiene información de una sucursal específica
     */
    @GetMapping("/branches/{branchId}")
    public ResponseEntity<BranchInfoDTO> getBranchInfo(
            @PathVariable UUID branchId) {
        return ResponseEntity.ok(useCase.getBranchInfo(branchId));
    }

    /**
     * Obtiene todos los productos de una sucursal
     */
    @GetMapping("/branches/{branchId}/products")
    public ResponseEntity<BranchProductsDTO> getProductsByBranch(
            @PathVariable UUID branchId) {
        return ResponseEntity.ok(useCase.getProductsByBranch(branchId));
    }
}
