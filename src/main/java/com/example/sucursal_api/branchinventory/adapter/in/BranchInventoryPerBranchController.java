package com.example.sucursal_api.branchinventory.adapter.in;

import com.example.sucursal_api.branch.port.out.BranchRepository;
import com.example.sucursal_api.branchinventory.dto.BranchStockRequestDTO;
import com.example.sucursal_api.branchinventory.dto.BranchStockTransferRequestDTO;
import com.example.sucursal_api.branch.domain.Branch;
import com.example.sucursal_api.chatbot.dto.BranchStockResponseDTO;
import com.example.sucursal_api.container.port.out.ContainerManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/branches/{branchId}/inventory")
public class BranchInventoryPerBranchController {

    private final BranchRepository branchRepository;
    private final ContainerManager containerManager;

    public BranchInventoryPerBranchController(BranchRepository branchRepository,
                                              ContainerManager containerManager) {
        this.branchRepository = branchRepository;
        this.containerManager = containerManager;
    }

    private WebClient clientForBranch(UUID branchId) {
        Branch branch = branchRepository.findById(branchId);
        String baseUrl = containerManager.getContainerUrl(branch.getSlug());
        return WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    @GetMapping("/branch-stock")
    public ResponseEntity<List<BranchStockResponseDTO>> listBranchStock(@PathVariable UUID branchId) {
        WebClient client = clientForBranch(branchId);
        List<BranchStockResponseDTO> stock = client.get()
                .uri("/api/branch-stock/branch/{branchId}", branchId)
                .retrieve()
                .bodyToFlux(BranchStockResponseDTO.class)
                .collectList()
                .block();
        return ResponseEntity.ok(stock);
    }

    @PostMapping("/branch-stock")
    public ResponseEntity<BranchStockResponseDTO> createBranchStock(@PathVariable UUID branchId,
                                                                    @RequestBody BranchStockRequestDTO dto) {
        WebClient client = clientForBranch(branchId);
        // Forzar branchId del body a coincidir con el de la URL
        BranchStockRequestDTO effectiveDto = new BranchStockRequestDTO(
                branchId,
                dto.productId(),
                dto.batchId(),
                dto.quantity(),
                dto.minimumStock()
        );

        BranchStockResponseDTO created = client.post()
                .uri("/api/branch-stock")
                .bodyValue(effectiveDto)
                .retrieve()
                .bodyToMono(BranchStockResponseDTO.class)
                .block();
        return ResponseEntity.ok(created);
    }

    @DeleteMapping("/branch-stock/{stockId}")
    public ResponseEntity<Void> deleteBranchStock(@PathVariable UUID branchId,
                                                  @PathVariable UUID stockId) {
        WebClient client = clientForBranch(branchId);
        client.delete()
                .uri("/api/branch-stock/{stockId}", stockId)
                .retrieve()
                .toBodilessEntity()
                .block();
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/branch-stock/{stockId}/transfer")
    public ResponseEntity<BranchStockResponseDTO> transferBranchStock(@PathVariable UUID branchId,
                                                                      @PathVariable UUID stockId,
                                                                      @RequestBody BranchStockTransferRequestDTO dto) {
        WebClient client = clientForBranch(branchId);
        BranchStockTransferRequestDTO effectiveDto = new BranchStockTransferRequestDTO(
                stockId,
                dto.targetBranchId(),
                dto.quantity()
        );

        BranchStockResponseDTO result = client.post()
                .uri("/api/branch-stock/transfer")
                .bodyValue(effectiveDto)
                .retrieve()
                .bodyToMono(BranchStockResponseDTO.class)
                .block();
        return ResponseEntity.ok(result);
    }

    // ===================== STOCK POR PRODUCTO =====================
    @GetMapping("/branch-stock/product/{productId}")
    public ResponseEntity<String> getBranchStockByProduct(@PathVariable UUID branchId,
                                                          @PathVariable UUID productId) {
        WebClient client = clientForBranch(branchId);
        String result = client.get()
                .uri("/api/branch-stock/product/{productId}", productId)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        return ResponseEntity.ok(result);
    }

    // ===================== BATCHES =====================
    @GetMapping("/batches")
    public ResponseEntity<String> listBatches(@PathVariable UUID branchId) {
        WebClient client = clientForBranch(branchId);
        String result = client.get()
                .uri("/api/batches")
                .retrieve()
                .bodyToMono(String.class)
                .block();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/batches/{batchId}")
    public ResponseEntity<String> getBatch(@PathVariable UUID branchId,
                                           @PathVariable UUID batchId) {
        WebClient client = clientForBranch(branchId);
        String result = client.get()
                .uri("/api/batches/{batchId}", batchId)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/batches/product/{productId}")
    public ResponseEntity<String> getBatchesByProduct(@PathVariable UUID branchId,
                                                      @PathVariable UUID productId) {
        WebClient client = clientForBranch(branchId);
        String result = client.get()
                .uri("/api/batches/product/{productId}", productId)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        return ResponseEntity.ok(result);
    }

    @PostMapping("/batches")
    public ResponseEntity<String> createBatch(@PathVariable UUID branchId,
                                              @RequestBody String body) {
        WebClient client = clientForBranch(branchId);
        String result = client.post()
                .uri("/api/batches")
                .header("Content-Type", "application/json")
                .bodyValue(body)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        return ResponseEntity.ok(result);
    }

    @PutMapping("/batches/{batchId}")
    public ResponseEntity<String> updateBatch(@PathVariable UUID branchId,
                                              @PathVariable UUID batchId,
                                              @RequestBody String body) {
        WebClient client = clientForBranch(branchId);
        String result = client.put()
                .uri("/api/batches/{batchId}", batchId)
                .header("Content-Type", "application/json")
                .bodyValue(body)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/batches/{batchId}")
    public ResponseEntity<Void> deleteBatch(@PathVariable UUID branchId,
                                            @PathVariable UUID batchId) {
        WebClient client = clientForBranch(branchId);
        client.delete()
                .uri("/api/batches/{batchId}", batchId)
                .retrieve()
                .toBodilessEntity()
                .block();
        return ResponseEntity.noContent().build();
    }

    // ===================== PRODUCTS =====================
    @GetMapping("/products")
    public ResponseEntity<String> listProducts(@PathVariable UUID branchId) {
        WebClient client = clientForBranch(branchId);
        String result = client.get()
                .uri("/api/products")
                .retrieve()
                .bodyToMono(String.class)
                .block();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/products/{productId}")
    public ResponseEntity<String> getProduct(@PathVariable UUID branchId,
                                             @PathVariable UUID productId) {
        WebClient client = clientForBranch(branchId);
        String result = client.get()
                .uri("/api/products/{productId}", productId)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        return ResponseEntity.ok(result);
    }

    @PostMapping("/products")
    public ResponseEntity<String> createProduct(@PathVariable UUID branchId,
                                                @RequestBody String body) {
        WebClient client = clientForBranch(branchId);
        String result = client.post()
                .uri("/api/products")
                .header("Content-Type", "application/json")
                .bodyValue(body)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        return ResponseEntity.ok(result);
    }

    @PutMapping("/products/{productId}")
    public ResponseEntity<String> updateProduct(@PathVariable UUID branchId,
                                                @PathVariable UUID productId,
                                                @RequestBody String body) {
        WebClient client = clientForBranch(branchId);
        String result = client.put()
                .uri("/api/products/{productId}", productId)
                .header("Content-Type", "application/json")
                .bodyValue(body)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/products/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable UUID branchId,
                                              @PathVariable UUID productId) {
        WebClient client = clientForBranch(branchId);
        client.delete()
                .uri("/api/products/{productId}", productId)
                .retrieve()
                .toBodilessEntity()
                .block();
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/products/sku/{sku}")
    public ResponseEntity<String> getProductBySku(@PathVariable UUID branchId,
                                                  @PathVariable String sku) {
        WebClient client = clientForBranch(branchId);
        String result = client.get()
                .uri("/api/products/sku/{sku}", sku)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/products/brand/{brand}")
    public ResponseEntity<String> getProductsByBrand(@PathVariable UUID branchId,
                                                     @PathVariable String brand) {
        WebClient client = clientForBranch(branchId);
        String result = client.get()
                .uri("/api/products/brand/{brand}", brand)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/products/category/{category}")
    public ResponseEntity<String> getProductsByCategory(@PathVariable UUID branchId,
                                                        @PathVariable String category) {
        WebClient client = clientForBranch(branchId);
        String result = client.get()
                .uri("/api/products/category/{category}", category)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/products/search")
    public ResponseEntity<String> searchProducts(@PathVariable UUID branchId,
                                                 @RequestParam String keyword) {
        WebClient client = clientForBranch(branchId);
        String result = client.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/products/search")
                        .queryParam("keyword", keyword)
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .block();
        return ResponseEntity.ok(result);
    }

    @PatchMapping("/products/{productId}/status")
    public ResponseEntity<String> updateProductStatus(@PathVariable UUID branchId,
                                                      @PathVariable UUID productId,
                                                      @RequestBody String body) {
        WebClient client = clientForBranch(branchId);
        String result = client.patch()
                .uri("/api/products/{productId}/status", productId)
                .header("Content-Type", "application/json")
                .bodyValue(body)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        return ResponseEntity.ok(result);
    }

    // ===================== STOCK ADICIONAL =====================
    @GetMapping("/branch-stock/batch/{batchId}")
    public ResponseEntity<String> getBranchStockByBatch(@PathVariable UUID branchId,
                                                        @PathVariable UUID batchId) {
        WebClient client = clientForBranch(branchId);
        String result = client.get()
                .uri("/api/branch-stock/batch/{batchId}", batchId)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/branch-stock/low-stock")
    public ResponseEntity<String> getLowStock(@PathVariable UUID branchId) {
        WebClient client = clientForBranch(branchId);
        String result = client.get()
                .uri("/api/branch-stock/low-stock")
                .retrieve()
                .bodyToMono(String.class)
                .block();
        return ResponseEntity.ok(result);
    }

    @PutMapping("/branch-stock/{stockId}")
    public ResponseEntity<String> updateBranchStock(@PathVariable UUID branchId,
                                                    @PathVariable UUID stockId,
                                                    @RequestBody String body) {
        WebClient client = clientForBranch(branchId);
        String result = client.put()
                .uri("/api/branch-stock/{stockId}", stockId)
                .header("Content-Type", "application/json")
                .bodyValue(body)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        return ResponseEntity.ok(result);
    }

    // ===================== BATCHES ADICIONAL =====================
    @GetMapping("/batches/expiring-soon")
    public ResponseEntity<String> getExpiringSoonBatches(@PathVariable UUID branchId) {
        WebClient client = clientForBranch(branchId);
        String result = client.get()
                .uri("/api/batches/expiring-soon")
                .retrieve()
                .bodyToMono(String.class)
                .block();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/batches/expired")
    public ResponseEntity<String> getExpiredBatches(@PathVariable UUID branchId) {
        WebClient client = clientForBranch(branchId);
        String result = client.get()
                .uri("/api/batches/expired")
                .retrieve()
                .bodyToMono(String.class)
                .block();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/batches/notifications")
    public ResponseEntity<String> getBatchNotifications(@PathVariable UUID branchId) {
        WebClient client = clientForBranch(branchId);
        String result = client.get()
                .uri("/api/batches/notifications")
                .retrieve()
                .bodyToMono(String.class)
                .block();
        return ResponseEntity.ok(result);
    }

    @PatchMapping("/batches/{batchId}/notification")
    public ResponseEntity<String> toggleBatchNotification(@PathVariable UUID branchId,
                                                          @PathVariable UUID batchId,
                                                          @RequestBody String body) {
        WebClient client = clientForBranch(branchId);
        String result = client.patch()
                .uri("/api/batches/{batchId}/notification", batchId)
                .header("Content-Type", "application/json")
                .bodyValue(body)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        return ResponseEntity.ok(result);
    }

    @PostMapping("/batches/deactivate-expired")
    public ResponseEntity<Void> deactivateExpiredBatches(@PathVariable UUID branchId) {
        WebClient client = clientForBranch(branchId);
        client.post()
                .uri("/api/batches/deactivate-expired")
                .retrieve()
                .toBodilessEntity()
                .block();
        return ResponseEntity.noContent().build();
    }

    // ===================== REPORTS =====================
    @GetMapping("/reports/stock/branch/{targetBranchId}")
    public ResponseEntity<String> getStockByBranch(@PathVariable UUID branchId,
                                                   @PathVariable UUID targetBranchId) {
        WebClient client = clientForBranch(branchId);
        String result = client.get()
                .uri("/api/reports/stock/branch/{targetBranchId}", targetBranchId)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/reports/stock/all-branches")
    public ResponseEntity<String> getAllStockByBranches(@PathVariable UUID branchId) {
        WebClient client = clientForBranch(branchId);
        String result = client.get()
                .uri("/api/reports/stock/all-branches")
                .retrieve()
                .bodyToMono(String.class)
                .block();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/reports/inventory-count/branch/{targetBranchId}")
    public ResponseEntity<String> getInventoryCountByBranch(@PathVariable UUID branchId,
                                                            @PathVariable UUID targetBranchId) {
        WebClient client = clientForBranch(branchId);
        String result = client.get()
                .uri("/api/reports/inventory-count/branch/{targetBranchId}", targetBranchId)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/reports/inventory-count/all-branches")
    public ResponseEntity<String> getAllInventoryCounts(@PathVariable UUID branchId) {
        WebClient client = clientForBranch(branchId);
        String result = client.get()
                .uri("/api/reports/inventory-count/all-branches")
                .retrieve()
                .bodyToMono(String.class)
                .block();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/reports/movements/branch/{targetBranchId}")
    public ResponseEntity<String> getMovementsByBranch(@PathVariable UUID branchId,
                                                       @PathVariable UUID targetBranchId,
                                                       @RequestParam String from,
                                                       @RequestParam String to) {
        WebClient client = clientForBranch(branchId);
        String result = client.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/reports/movements/branch/{targetBranchId}")
                        .queryParam("from", from)
                        .queryParam("to", to)
                        .build(targetBranchId))
                .retrieve()
                .bodyToMono(String.class)
                .block();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/reports/movements/branch/{targetBranchId}/type/{type}")
    public ResponseEntity<String> getMovementsByType(@PathVariable UUID branchId,
                                                     @PathVariable UUID targetBranchId,
                                                     @PathVariable String type,
                                                     @RequestParam String from,
                                                     @RequestParam String to) {
        WebClient client = clientForBranch(branchId);
        String result = client.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/reports/movements/branch/{targetBranchId}/type/{type}")
                        .queryParam("from", from)
                        .queryParam("to", to)
                        .build(targetBranchId, type))
                .retrieve()
                .bodyToMono(String.class)
                .block();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/reports/movements/product/{productId}")
    public ResponseEntity<String> getMovementsByProduct(@PathVariable UUID branchId,
                                                        @PathVariable UUID productId,
                                                        @RequestParam String from,
                                                        @RequestParam String to) {
        WebClient client = clientForBranch(branchId);
        String result = client.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/reports/movements/product/{productId}")
                        .queryParam("from", from)
                        .queryParam("to", to)
                        .build(productId))
                .retrieve()
                .bodyToMono(String.class)
                .block();
        return ResponseEntity.ok(result);
    }
}
