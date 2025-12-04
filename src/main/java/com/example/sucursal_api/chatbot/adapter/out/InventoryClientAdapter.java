package com.example.sucursal_api.chatbot.adapter.out;

import com.example.sucursal_api.chatbot.dto.ProductInfoDTO;
import com.example.sucursal_api.chatbot.port.out.InventoryClient;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class InventoryClientAdapter implements InventoryClient {

    private static final Logger log = LoggerFactory.getLogger(InventoryClientAdapter.class);
    
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public InventoryClientAdapter() {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public List<ProductInfoDTO> getAllProducts(String inventoryUrl) {
        try {
            String url = inventoryUrl + "/api/products";
            log.debug("Obteniendo productos de: {}", url);
            
            String response = restTemplate.getForObject(url, String.class);
            return parseProductsResponse(response);
        } catch (RestClientException e) {
            log.error("Error obteniendo productos de {}: {}", inventoryUrl, e.getMessage());
            return List.of();
        }
    }

    @Override
    public List<ProductInfoDTO> searchProductsByName(String inventoryUrl, String productName) {
        try {
            String url = inventoryUrl + "/api/products/search?q=" + productName;
            log.debug("Buscando productos por nombre en: {}", url);
            
            String response = restTemplate.getForObject(url, String.class);
            return parseProductsResponse(response);
        } catch (RestClientException e) {
            log.error("Error buscando productos por nombre en {}: {}", inventoryUrl, e.getMessage());
            return List.of();
        }
    }

    @Override
    public List<ProductInfoDTO> searchProductsByCategory(String inventoryUrl, String category) {
        try {
            String url = inventoryUrl + "/api/products/category/" + category;
            log.debug("Buscando productos por categoría en: {}", url);
            
            String response = restTemplate.getForObject(url, String.class);
            return parseProductsResponse(response);
        } catch (RestClientException e) {
            log.error("Error buscando productos por categoría en {}: {}", inventoryUrl, e.getMessage());
            return List.of();
        }
    }

    @Override
    public List<ProductInfoDTO> searchProductsByBrand(String inventoryUrl, String brand) {
        try {
            String url = inventoryUrl + "/api/products/brand/" + brand;
            log.debug("Buscando productos por marca en: {}", url);
            
            String response = restTemplate.getForObject(url, String.class);
            return parseProductsResponse(response);
        } catch (RestClientException e) {
            log.error("Error buscando productos por marca en {}: {}", inventoryUrl, e.getMessage());
            return List.of();
        }
    }

    @Override
    public int getProductStock(String inventoryUrl, String productId) {
        try {
            String url = inventoryUrl + "/api/branch-stock/product/" + productId;
            log.debug("Obteniendo stock de producto en: {}", url);
            
            String response = restTemplate.getForObject(url, String.class);
            JsonNode root = objectMapper.readTree(response);
            
            int totalStock = 0;
            if (root.isArray()) {
                for (JsonNode stockNode : root) {
                    totalStock += stockNode.path("quantity").asInt(0);
                }
            }
            return totalStock;
        } catch (Exception e) {
            log.error("Error obteniendo stock en {}: {}", inventoryUrl, e.getMessage());
            return 0;
        }
    }

    @Override
    public boolean isInventoryAvailable(String inventoryUrl) {
        try {
            String url = inventoryUrl + "/api/products";
            restTemplate.getForObject(url, String.class);
            return true;
        } catch (Exception e) {
            log.debug("Inventario no disponible en {}: {}", inventoryUrl, e.getMessage());
            return false;
        }
    }

    private List<ProductInfoDTO> parseProductsResponse(String response) {
        if (response == null || response.isEmpty()) {
            return List.of();
        }
        
        try {
            JsonNode root = objectMapper.readTree(response);
            List<ProductInfoDTO> products = new ArrayList<>();
            
            if (root.isArray()) {
                for (JsonNode node : root) {
                    products.add(parseProductNode(node));
                }
            }
            
            return products;
        } catch (Exception e) {
            log.error("Error parseando respuesta de productos: {}", e.getMessage());
            return List.of();
        }
    }

    private ProductInfoDTO parseProductNode(JsonNode node) {
        return new ProductInfoDTO(
                UUID.fromString(node.path("id").asText()),
                node.path("name").asText(),
                node.path("description").asText(null),
                node.path("sku").asText(),
                node.path("brand").asText(),
                node.path("category").asText(null),
                new BigDecimal(node.path("unitPrice").asText("0")),
                node.path("unit").asText(),
                node.path("availableQuantity").asInt(0)
        );
    }
}
