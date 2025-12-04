package com.example.sucursal_api.branch.application;

import com.example.sucursal_api.branch.application.mapper.BranchMapper;
import com.example.sucursal_api.branch.domain.Branch;
import com.example.sucursal_api.branch.dto.BranchRequestDTO;
import com.example.sucursal_api.branch.dto.BranchResponseDTO;
import com.example.sucursal_api.branch.dto.BranchStatusUpdateDTO;
import com.example.sucursal_api.branch.dto.BranchUpdateDTO;
import com.example.sucursal_api.branch.port.in.BranchUseCase;
import com.example.sucursal_api.branch.port.out.BranchRepository;
import com.example.sucursal_api.container.port.out.ContainerInfo;
import com.example.sucursal_api.container.port.out.ContainerManager;
import com.example.sucursal_api.image.domain.BranchImage;
import com.example.sucursal_api.image.port.out.BranchImageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.text.Normalizer;
import java.util.List;
import java.util.UUID;

@Service
public class BranchService implements BranchUseCase {

    private static final Logger log = LoggerFactory.getLogger(BranchService.class);

    private final BranchRepository repo;
    private final BranchMapper mapper;
    private final BranchImageRepository imageRepo;
    private final ContainerManager containerManager;

    public BranchService(BranchRepository repo, BranchMapper mapper, 
                         BranchImageRepository imageRepo, ContainerManager containerManager) {
        this.repo = repo;
        this.mapper = mapper;
        this.imageRepo = imageRepo;
        this.containerManager = containerManager;
    }

    // Helper para convertir Branch a DTO con imagen de portada
    private BranchResponseDTO toResponseWithCover(Branch branch) {
        BranchImage coverImage = imageRepo.findCoverByBranch(branch.getId());
        String coverUrl = coverImage != null ? coverImage.getUrl() : null;
        return new BranchResponseDTO(
                branch.getId(),
                branch.getName(),
                branch.getSlug(),
                branch.getAddress(),
                branch.getPrimaryPhone(),
                branch.getLat(),
                branch.getLng(),
                branch.isActive(),
                coverUrl
        );
    }

    @Override
    @Transactional
    public BranchResponseDTO create(BranchRequestDTO dto) {
        String slug = (dto.slug() == null || dto.slug().isBlank())
                ? slugify(dto.name())
                : slugify(dto.slug());

        if (repo.existsBySlug(slug)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El slug ya existe: " + slug);
        }

        Branch branch = new Branch(
                null,
                dto.name(),
                slug,
                dto.address(),
                dto.primaryPhone(),
                dto.lat(),
                dto.lng(),
                true
        );

        Branch saved = repo.save(branch);

        // crear contenedor de inventario para la sucursal
        try {
            ContainerInfo containerInfo = containerManager.createInventoryContainer(slug);
            log.info("Contenedor de inventario creado para sucursal {}: {}", 
                     slug, containerInfo.internalUrl());
        } catch (Exception e) {
            log.error("Error creando contenedor para sucursal {}: {}", slug, e.getMessage());
        }

        return toResponseWithCover(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public BranchResponseDTO getById(UUID id) {
        Branch b = repo.findById(id);
        return toResponseWithCover(b);
    }

    @Override
    @Transactional(readOnly = true)
    public BranchResponseDTO getBySlug(String slug) {
        Branch b = repo.findBySlug(slug);
        return toResponseWithCover(b);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BranchResponseDTO> list() {
        return repo.findAll().stream().map(this::toResponseWithCover).filter(repo -> repo.active()).toList();
    }

    @Override
    @Transactional
    public BranchResponseDTO update(UUID id, BranchUpdateDTO dto) {
        Branch current = repo.findById(id);

        String newSlug = dto.slug() != null && !dto.slug().isBlank()
                ? slugify(dto.slug())
                : current.getSlug();

        if (!newSlug.equals(current.getSlug()) && repo.existsBySlugAndIdNot(newSlug, id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El slug ya existe: " + newSlug);
        }

        current.setName(dto.name() != null ? dto.name() : current.getName());
        current.setSlug(newSlug);
        current.setAddress(dto.address() != null ? dto.address() : current.getAddress());
        current.setPrimaryPhone(dto.primaryPhone() != null ? dto.primaryPhone() : current.getPrimaryPhone());
        if (dto.lat() != null) current.setLat(dto.lat());
        if (dto.lng() != null) current.setLng(dto.lng());

        Branch saved = repo.save(current);
        return toResponseWithCover(saved);
    }

    @Override
    @Transactional
    public BranchResponseDTO updateStatus(UUID id, BranchStatusUpdateDTO dto) {
        Branch current = repo.findById(id);
        current.setActive(dto.active());
        Branch saved = repo.save(current);

        // Iniciar o detener contenedor según el nuevo estado
        try {
            if (dto.active()) {
                containerManager.startInventoryContainer(current.getSlug());
                log.info("Contenedor de inventario iniciado para sucursal: {}", current.getSlug());
            } else {
                containerManager.stopInventoryContainer(current.getSlug());
                log.info("Contenedor de inventario detenido para sucursal: {}", current.getSlug());
            }
        } catch (Exception e) {
            log.error("Error gestionando contenedor para sucursal {}: {}", current.getSlug(), e.getMessage());
        }

        return toResponseWithCover(saved);
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        Branch branch = repo.findById(id);
        
        // Eliminar contenedor antes de borrar la sucursal
        try {
            containerManager.removeInventoryContainer(branch.getSlug());
            log.info("Contenedor de inventario eliminado para sucursal: {}", branch.getSlug());
        } catch (Exception e) {
            log.warn("No se pudo eliminar contenedor para {}: {}", branch.getSlug(), e.getMessage());
        }
        
        repo.delete(id);
    }

    private String slugify(String input) {
        String nfd = Normalizer.normalize(input, Normalizer.Form.NFD);
        String sinTildes = nfd.replaceAll("\\p{M}+", "");
        String lower = sinTildes.toLowerCase();
        String onlySafe = lower.replaceAll("[^a-z0-9]+", "-");
        String compact = onlySafe.replaceAll("-{2,}", "-");
        return compact.replaceAll("^-|-$", "");
    }
}
