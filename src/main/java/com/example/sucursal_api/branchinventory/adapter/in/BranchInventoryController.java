package com.example.sucursal_api.branchinventory.adapter.in;

import com.example.sucursal_api.chatbot.dto.BatchResponseDTO;
import com.example.sucursal_api.chatbot.dto.BranchStockResponseDTO;
import com.example.sucursal_api.chatbot.service.InventoryProxyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/branch-inventory")
public class BranchInventoryController {

    private final InventoryProxyService inventoryProxyService;

    public BranchInventoryController(InventoryProxyService inventoryProxyService) {
        this.inventoryProxyService = inventoryProxyService;
    }

    @GetMapping("/batches/product/{productId}")
    public ResponseEntity<List<BatchResponseDTO>> listBatchesByProduct(@PathVariable UUID productId) {
        return ResponseEntity.ok(inventoryProxyService.listBatchesByProduct(productId));
    }

    @GetMapping("/branch-stock/product/{productId}")
    public ResponseEntity<List<BranchStockResponseDTO>> listBranchStockByProduct(@PathVariable UUID productId) {
        return ResponseEntity.ok(inventoryProxyService.listStockByProduct(productId));
    }
}
