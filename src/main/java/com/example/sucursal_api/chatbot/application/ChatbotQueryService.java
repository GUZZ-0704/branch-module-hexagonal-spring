package com.example.sucursal_api.chatbot.application;
import com.example.sucursal_api.branch.domain.Branch;
import com.example.sucursal_api.branch.port.out.BranchRepository;
import com.example.sucursal_api.chatbot.dto.*;
import com.example.sucursal_api.chatbot.port.in.ChatbotQueryUseCase;
import com.example.sucursal_api.chatbot.port.out.InventoryClient;
import com.example.sucursal_api.container.port.out.ContainerManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ChatbotQueryService implements ChatbotQueryUseCase {

    private static final Logger log = LoggerFactory.getLogger(ChatbotQueryService.class);

    private final BranchRepository branchRepository;
    private final ContainerManager containerManager;
    private final InventoryClient inventoryClient;

    public ChatbotQueryService(BranchRepository branchRepository,
                                ContainerManager containerManager,
                                InventoryClient inventoryClient) {
        this.branchRepository = branchRepository;
        this.containerManager = containerManager;
        this.inventoryClient = inventoryClient;
    }

    @Override
    public ProductSearchResponseDTO searchProducts(ProductSearchRequestDTO request) {
        List<Branch> branches;
        
        if (request.branchId() != null && !request.searchAllBranches()) {
            // Buscar solo en una sucursal específica
            Branch branch = branchRepository.findById(request.branchId());
            branches = List.of(branch);
        } else {
            // Buscar en todas las sucursales activas
            branches = branchRepository.findAll().stream()
                    .filter(Branch::isActive)
                    .collect(Collectors.toList());
        }

        List<BranchProductsDTO> results = new ArrayList<>();
        int totalProducts = 0;

        for (Branch branch : branches) {
            if (!branch.isActive()) continue;
            
            String inventoryUrl = containerManager.getContainerUrl(branch.getSlug());
            
            if (!inventoryClient.isInventoryAvailable(inventoryUrl)) {
                log.warn("Inventario no disponible para sucursal: {}", branch.getSlug());
                continue;
            }

            List<ProductInfoDTO> products = searchProductsInInventory(inventoryUrl, request);
            
            if (!products.isEmpty()) {
                results.add(new BranchProductsDTO(
                        branch.getId(),
                        branch.getName(),
                        branch.getSlug(),
                        branch.getAddress(),
                        branch.isActive(),
                        products
                ));
                totalProducts += products.size();
            }
        }

        String queryDescription = buildQueryDescription(request);
        
        return new ProductSearchResponseDTO(
                queryDescription,
                totalProducts,
                branches.size(),
                results
        );
    }

    @Override
    public BranchListResponseDTO getActiveBranches() {
        List<Branch> activeBranches = branchRepository.findAll().stream()
                .filter(Branch::isActive)
                .collect(Collectors.toList());

        List<BranchInfoDTO> branchInfos = activeBranches.stream()
                .map(this::toBranchInfoDTO)
                .collect(Collectors.toList());

        return new BranchListResponseDTO(branchInfos.size(), branchInfos);
    }

    @Override
    public BranchInfoDTO getBranchInfo(UUID branchId) {
        Branch branch = branchRepository.findById(branchId);
        return toBranchInfoDTO(branch);
    }

    @Override
    public BranchProductsDTO getProductsByBranch(UUID branchId) {
        Branch branch = branchRepository.findById(branchId);
        
        if (!branch.isActive()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
                    "La sucursal no está activa");
        }

        String inventoryUrl = containerManager.getContainerUrl(branch.getSlug());
        
        if (!inventoryClient.isInventoryAvailable(inventoryUrl)) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, 
                    "El inventario de la sucursal no está disponible");
        }

        List<ProductInfoDTO> products = inventoryClient.getAllProducts(inventoryUrl);

        return new BranchProductsDTO(
                branch.getId(),
                branch.getName(),
                branch.getSlug(),
                branch.getAddress(),
                branch.isActive(),
                products
        );
    }

    @Override
    public ProductSearchResponseDTO findProductAvailability(String productName) {
        ProductSearchRequestDTO request = new ProductSearchRequestDTO(
                productName, null, null, null, true
        );
        return searchProducts(request);
    }

    private List<ProductInfoDTO> searchProductsInInventory(String inventoryUrl, ProductSearchRequestDTO request) {
        if (request.productName() != null && !request.productName().isBlank()) {
            return inventoryClient.searchProductsByName(inventoryUrl, request.productName());
        }
        if (request.category() != null && !request.category().isBlank()) {
            return inventoryClient.searchProductsByCategory(inventoryUrl, request.category());
        }
        if (request.brand() != null && !request.brand().isBlank()) {
            return inventoryClient.searchProductsByBrand(inventoryUrl, request.brand());
        }
        return inventoryClient.getAllProducts(inventoryUrl);
    }

    private String buildQueryDescription(ProductSearchRequestDTO request) {
        StringBuilder sb = new StringBuilder("Búsqueda de productos");
        if (request.productName() != null) {
            sb.append(" con nombre '").append(request.productName()).append("'");
        }
        if (request.category() != null) {
            sb.append(" en categoría '").append(request.category()).append("'");
        }
        if (request.brand() != null) {
            sb.append(" de marca '").append(request.brand()).append("'");
        }
        return sb.toString();
    }

    private BranchInfoDTO toBranchInfoDTO(Branch branch) {
        return new BranchInfoDTO(
                branch.getId(),
                branch.getName(),
                branch.getSlug(),
                branch.getAddress(),
                branch.getPrimaryPhone(),
                branch.getLat(),
                branch.getLng(),
                branch.isActive()
        );
    }
}
