package com.example.sucursal_api.chatbot.service;

import com.example.sucursal_api.chatbot.dto.BranchStockResponseDTO;
import com.example.sucursal_api.chatbot.dto.ProductResponseDTO;
import com.example.sucursal_api.chatbot.dto.StockMovementResponseDTO;
import com.example.sucursal_api.chatbot.dto.TransferRequestDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class InventoryProxyService {

    public List<ProductResponseDTO> searchProducts(String keyword) {
        // TODO reenviar a INVENTARIO
        return null;
    }

    public ProductResponseDTO getProductBySku(String sku) {
        // TODO reenviar a INVENTARIO
        return null;
    }

    public List<BranchStockResponseDTO> listStockByBranch(UUID branchId) {
        // TODO reenviar a INVENTARIO
        return null;
    }

    public List<BranchStockResponseDTO> listStockByProduct(UUID productId) {
        // TODO reenviar a INVENTARIO
        return null;
    }

    public StockMovementResponseDTO requestTransfer(TransferRequestDTO dto) {
        // TODO reenviar a INVENTARIO
        return null;
    }
}
