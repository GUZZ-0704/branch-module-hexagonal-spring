package com.example.sucursal_api.chatbot;

import com.example.sucursal_api.branch.dto.BranchResponseDTO;
import com.example.sucursal_api.chatbot.dto.*;
import com.example.sucursal_api.chatbot.service.InventoryProxyService;
import com.example.sucursal_api.chatbot.service.BranchProxyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class SucursalProxyController {

    private final InventoryProxyService inventoryProxy;
    private final BranchProxyService branchProxy;

    public SucursalProxyController(
            InventoryProxyService inventoryProxy,
            BranchProxyService branchProxy
    ) {
        this.inventoryProxy = inventoryProxy;
        this.branchProxy = branchProxy;
    }

    @GetMapping("/products/search")
    public ResponseEntity<List<ProductResponseDTO>> searchProducts(@RequestParam String keyword) {
        return ResponseEntity.ok(inventoryProxy.searchProducts(keyword));
    }

    @GetMapping("/products/sku/{sku}")
    public ResponseEntity<ProductResponseDTO> getProductBySku(@PathVariable String sku) {
        return ResponseEntity.ok(inventoryProxy.getProductBySku(sku));
    }

    @GetMapping("/branch-stock/branch/{branchId}")
    public ResponseEntity<List<BranchStockResponseDTO>> stockByBranch(@PathVariable UUID branchId) {
        return ResponseEntity.ok(inventoryProxy.listStockByBranch(branchId));
    }

    @GetMapping("/branch-stock/product/{productId}")
    public ResponseEntity<List<BranchStockResponseDTO>> stockByProduct(@PathVariable UUID productId) {
        return ResponseEntity.ok(inventoryProxy.listStockByProduct(productId));
    }

    @PostMapping("/movements/transfer")
    public ResponseEntity<StockMovementResponseDTO> transfer(@RequestBody TransferRequestDTO dto) {
        return ResponseEntity.ok(inventoryProxy.requestTransfer(dto));
    }

    @GetMapping("/branches")
    public ResponseEntity<List<BranchResponseDTO>> listBranches() {
        return ResponseEntity.ok(branchProxy.listBranches());
    }

    @GetMapping("/branches/{id}")
    public ResponseEntity<BranchResponseDTO> getBranch(@PathVariable UUID id) {
        return ResponseEntity.ok(branchProxy.getBranchById(id));
    }
}
