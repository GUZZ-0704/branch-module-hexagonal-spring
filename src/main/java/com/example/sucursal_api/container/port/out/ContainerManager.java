package com.example.sucursal_api.container.port.out;

public interface ContainerManager {

    /**
     * Crea y arranca un contenedor de inventario para una sucursal
     * @param branchSlug identificador único (se usa como nombre del contenedor)
     * @return información del contenedor creado
     */
    ContainerInfo createInventoryContainer(String branchSlug);

    /**
     * Detiene y elimina el contenedor de una sucursal
     */
    void removeInventoryContainer(String branchSlug);

    /**
     * Detiene el contenedor (sin eliminarlo)
     */
    void stopInventoryContainer(String branchSlug);

    /**
     * Arranca un contenedor detenido
     */
    void startInventoryContainer(String branchSlug);

    /**
     * Verifica si el contenedor existe y está corriendo
     */
    boolean isContainerRunning(String branchSlug);

    /**
     * Obtiene la URL interna del contenedor para comunicación
     */
    String getContainerUrl(String branchSlug);
}
