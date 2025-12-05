package com.example.sucursal_api.chatbot.service;

import com.example.sucursal_api.chatbot.dto.BranchStockResponseDTO;
import com.example.sucursal_api.chatbot.dto.ProductResponseDTO;
import com.example.sucursal_api.chatbot.dto.StockMovementResponseDTO;
import com.example.sucursal_api.chatbot.dto.TransferRequestDTO;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.UUID;

@Service
public class InventoryProxyService {

    private final WebClient webClient;

    public InventoryProxyService(
            @Value("${external.inventory-service.url}") String inventoryUrl
    ) {
        this.webClient = WebClient.builder()
                .baseUrl(inventoryUrl)
                .build();
    }

    public List<ProductResponseDTO> searchProducts(String keyword) {
        System.out.println("Searching products with keyword: " + keyword);
        return webClient.get()
                .uri(uri -> uri.path("/api/products/search")
                        .queryParam("keyword", keyword)
                        .build())
                .retrieve()
                .bodyToFlux(ProductResponseDTO.class)
                .collectList()
                .block();
    }

    public ProductResponseDTO getProductBySku(String sku) {
        return webClient.get()
                .uri("/api/products/sku/{sku}", sku)
                .retrieve()
                .bodyToMono(ProductResponseDTO.class)
                .block();
    }

    public List<BranchStockResponseDTO> listStockByBranch(UUID branchId) {
        return webClient.get()
                .uri("/api/branch-stock/branch/{branchId}", branchId)
                .retrieve()
                .bodyToFlux(BranchStockResponseDTO.class)
                .collectList()
                .block();
    }

    public List<BranchStockResponseDTO> listStockByProduct(UUID productId) {
        return webClient.get()
                .uri("/api/branch-stock/product/{productId}", productId)
                .retrieve()
                .bodyToFlux(BranchStockResponseDTO.class)
                .collectList()
                .block();
    }

    public StockMovementResponseDTO requestTransfer(TransferRequestDTO dto) {
        return webClient.post()
                .uri("/api/movements/transfer")
                .bodyValue(dto)
                .retrieve()
                .bodyToMono(StockMovementResponseDTO.class)
                .block();
    }
}
