package com.example.sucursal_api.chatbot.port.out;

import com.example.sucursal_api.chatbot.dto.ProductInfoDTO;

import java.util.List;

public interface InventoryClient {
    
    /**
     * Obtiene todos los productos de un contenedor de inventario
     * @param inventoryUrl URL del contenedor (ej: http://inv-sucursal-centro:8080)
     */
    List<ProductInfoDTO> getAllProducts(String inventoryUrl);
    
    /**
     * Busca productos por nombre en un contenedor de inventario
     */
    List<ProductInfoDTO> searchProductsByName(String inventoryUrl, String productName);
    
    /**
     * Busca productos por categoría
     */
    List<ProductInfoDTO> searchProductsByCategory(String inventoryUrl, String category);
    
    /**
     * Busca productos por marca
     */
    List<ProductInfoDTO> searchProductsByBrand(String inventoryUrl, String brand);
    
    /**
     * Obtiene el stock disponible de un producto específico
     */
    int getProductStock(String inventoryUrl, String productId);
    
    /**
     * Verifica si el inventario está disponible (health check)
     */
    boolean isInventoryAvailable(String inventoryUrl);
}
