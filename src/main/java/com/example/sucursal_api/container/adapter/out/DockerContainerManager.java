package com.example.sucursal_api.container.adapter.out;

import com.example.sucursal_api.container.port.out.ContainerInfo;
import com.example.sucursal_api.container.port.out.ContainerManager;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.model.ContainerNetwork;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class DockerContainerManager implements ContainerManager {

    private static final Logger log = LoggerFactory.getLogger(DockerContainerManager.class);

    @Value("${docker.host:tcp://localhost:2375}")
    private String dockerHost;

    @Value("${docker.network.name:sucursales-net}")
    private String networkName;

    @Value("${docker.inventory.image:inventory-service:latest}")
    private String inventoryImage;

    @Value("${docker.inventory.internal-port:8080}")
    private int internalPort;

    @Value("${docker.inventory.port-range-start:9000}")
    private int portRangeStart;

    @Value("${spring.datasource.url:jdbc:postgresql://localhost:5432/sucursales}")
    private String datasourceUrl;

    @Value("${spring.datasource.username:postgres}")
    private String datasourceUsername;

    @Value("${spring.datasource.password:1234}")
    private String datasourcePassword;

    private DockerClient dockerClient;
    private final AtomicInteger portCounter = new AtomicInteger(0);
    private final Map<String, ContainerInfo> containerRegistry = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        try {
            var configBuilder = DefaultDockerClientConfig.createDefaultConfigBuilder();
            
            // Configurar el host de Docker
            if (dockerHost != null && !dockerHost.isEmpty()) {
                configBuilder.withDockerHost(dockerHost);
            }
            
            var config = configBuilder.build();

            var httpClientBuilder = new ApacheDockerHttpClient.Builder()
                    .dockerHost(config.getDockerHost())
                    .maxConnections(100)
                    .connectionTimeout(Duration.ofSeconds(30))
                    .responseTimeout(Duration.ofSeconds(45));

            var httpClient = httpClientBuilder.build();

            this.dockerClient = DockerClientImpl.getInstance(config, httpClient);
            
            // Verificar conexión
            dockerClient.pingCmd().exec();

            ensureNetworkExists();
            log.info("DockerContainerManager inicializado. Host: {}, Red: {}", dockerHost, networkName);
        } catch (Exception e) {
            log.error("Error inicializando DockerContainerManager: {}. Los contenedores no se gestionarán automáticamente.", e.getMessage());
            this.dockerClient = null;
        }
    }

    private void ensureNetworkExists() {
        if (dockerClient == null) return;

        try {
            var networks = dockerClient.listNetworksCmd()
                    .withNameFilter(networkName)
                    .exec();

            if (networks.isEmpty()) {
                log.info("Creando red Docker: {}", networkName);
                dockerClient.createNetworkCmd()
                        .withName(networkName)
                        .withDriver("bridge")
                        .exec();
            }
        } catch (Exception e) {
            log.warn("No se pudo verificar/crear la red Docker: {}", e.getMessage());
        }
    }

    @Override
    public ContainerInfo createInventoryContainer(String branchSlug) {
        if (dockerClient == null) {
            log.warn("DockerClient no disponible. Saltando creación de contenedor para: {}", branchSlug);
            return createMockContainerInfo(branchSlug);
        }

        String containerName = "inv-" + sanitizeSlug(branchSlug);
        String networkAlias = containerName;

        if (containerExists(containerName)) {
            log.info("Contenedor {} ya existe, iniciándolo...", containerName);
            startInventoryContainer(branchSlug);
            return containerRegistry.getOrDefault(branchSlug, createMockContainerInfo(branchSlug));
        }

        int hostPort = portRangeStart + portCounter.getAndIncrement();
        
        // localhost a host.docker.internal para que el contenedor acceda al host
        String containerDatasourceUrl = datasourceUrl
                .replace("localhost", "host.docker.internal")
                .replace("127.0.0.1", "host.docker.internal")
                .replace("sucursales", "inventario");

        try {
            CreateContainerResponse container = dockerClient.createContainerCmd(inventoryImage)
                    .withName(containerName)
                    .withHostConfig(HostConfig.newHostConfig()
                            .withPortBindings(new PortBinding(
                                    Ports.Binding.bindPort(hostPort),
                                    ExposedPort.tcp(internalPort)))
                            .withNetworkMode(networkName)
                            .withExtraHosts("host.docker.internal:host-gateway"))
                    .withEnv(
                            "BRANCH_SLUG=" + branchSlug,
                            "SERVER_PORT=" + internalPort,
                            "SPRING_DATASOURCE_URL=" + containerDatasourceUrl,
                            "SPRING_DATASOURCE_USERNAME=" + datasourceUsername,
                            "SPRING_DATASOURCE_PASSWORD=" + datasourcePassword,
                            "SPRING_JPA_HIBERNATE_DDL_AUTO=update"
                    )
                    .withExposedPorts(ExposedPort.tcp(internalPort))
                    .exec();

            try {
                dockerClient.connectToNetworkCmd()
                        .withNetworkId(networkName)
                        .withContainerId(container.getId())
                        .withContainerNetwork(new ContainerNetwork()
                                .withAliases(networkAlias))
                        .exec();
            } catch (Exception e) {
                log.debug("Contenedor ya conectado a la red o conexión automática");
            }

            dockerClient.startContainerCmd(container.getId()).exec();

            ContainerInfo info = new ContainerInfo(
                    container.getId(),
                    containerName,
                    networkAlias,
                    "http://" + networkAlias + ":" + internalPort,
                    hostPort
            );

            containerRegistry.put(branchSlug, info);
            log.info("Contenedor creado: {} ({}), puerto host: {}, URL interna: {}",
                    containerName, container.getId().substring(0, 12), hostPort, info.internalUrl());

            return info;

        } catch (Exception e) {
            log.error("Error creando contenedor para {}: {}", branchSlug, e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "No se pudo crear el contenedor de inventario: " + e.getMessage());
        }
    }

    @Override
    public void removeInventoryContainer(String branchSlug) {
        if (dockerClient == null) {
            log.warn("DockerClient no disponible. Saltando eliminación de contenedor para: {}", branchSlug);
            return;
        }

        String containerName = "inv-" + sanitizeSlug(branchSlug);

        try {
            var containers = dockerClient.listContainersCmd()
                    .withShowAll(true)
                    .withNameFilter(List.of(containerName))
                    .exec();

            if (containers.isEmpty()) {
                log.warn("No se encontró contenedor: {}", containerName);
                return;
            }

            String containerId = containers.get(0).getId();

            if ("running".equalsIgnoreCase(containers.get(0).getState())) {
                dockerClient.stopContainerCmd(containerId).exec();
            }

            dockerClient.removeContainerCmd(containerId).exec();
            containerRegistry.remove(branchSlug);
            log.info("Contenedor eliminado: {}", containerName);

        } catch (Exception e) {
            log.error("Error eliminando contenedor {}: {}", containerName, e.getMessage());
        }
    }

    @Override
    public void stopInventoryContainer(String branchSlug) {
        if (dockerClient == null) return;

        String containerName = "inv-" + sanitizeSlug(branchSlug);

        try {
            var containers = dockerClient.listContainersCmd()
                    .withNameFilter(List.of(containerName))
                    .exec();

            if (!containers.isEmpty()) {
                dockerClient.stopContainerCmd(containers.get(0).getId()).exec();
                log.info("Contenedor detenido: {}", containerName);
            }
        } catch (Exception e) {
            log.error("Error deteniendo contenedor {}: {}", containerName, e.getMessage());
        }
    }

    @Override
    public void startInventoryContainer(String branchSlug) {
        if (dockerClient == null) return;

        String containerName = "inv-" + sanitizeSlug(branchSlug);

        try {
            var containers = dockerClient.listContainersCmd()
                    .withShowAll(true)
                    .withNameFilter(List.of(containerName))
                    .exec();

            if (!containers.isEmpty()) {
                String containerId = containers.get(0).getId();
                if (!"running".equalsIgnoreCase(containers.get(0).getState())) {
                    dockerClient.startContainerCmd(containerId).exec();
                    log.info("Contenedor iniciado: {}", containerName);
                }
            }
        } catch (Exception e) {
            log.error("Error iniciando contenedor {}: {}", containerName, e.getMessage());
        }
    }

    @Override
    public boolean isContainerRunning(String branchSlug) {
        if (dockerClient == null) return false;

        String containerName = "inv-" + sanitizeSlug(branchSlug);

        try {
            var containers = dockerClient.listContainersCmd()
                    .withNameFilter(List.of(containerName))
                    .exec();
            return !containers.isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public String getContainerUrl(String branchSlug) {
        ContainerInfo info = containerRegistry.get(branchSlug);
        if (info != null) {
            return info.internalUrl();
        }
        String containerName = "inv-" + sanitizeSlug(branchSlug);
        return "http://" + containerName + ":" + internalPort;
    }

    private boolean containerExists(String containerName) {
        if (dockerClient == null) return false;

        try {
            var containers = dockerClient.listContainersCmd()
                    .withShowAll(true)
                    .withNameFilter(List.of(containerName))
                    .exec();
            return !containers.isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    private String sanitizeSlug(String slug) {
        return slug.toLowerCase()
                .replaceAll("[^a-z0-9-]", "-")
                .replaceAll("-+", "-")
                .replaceAll("^-|-$", "");
    }

    private ContainerInfo createMockContainerInfo(String branchSlug) {
        String containerName = "inv-" + sanitizeSlug(branchSlug);
        return new ContainerInfo(
                "mock-" + branchSlug,
                containerName,
                containerName,
                "http://" + containerName + ":" + internalPort,
                portRangeStart + portCounter.get()
        );
    }
}
