package com.example.sucursal_api.container.port.out;

public record ContainerInfo(
        String containerId,
        String containerName,
        String networkAlias,
        String internalUrl,
        int hostPort
) {}
