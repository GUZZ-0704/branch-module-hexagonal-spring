package com.example.sucursal_api.image.application;

import com.example.sucursal_api.branch.port.out.BranchRepository;
import com.example.sucursal_api.file.StorageService;
import com.example.sucursal_api.image.application.mapper.BranchImageMapper;
import com.example.sucursal_api.image.domain.BranchImage;
import com.example.sucursal_api.image.dto.BranchImageRequestDTO;
import com.example.sucursal_api.image.dto.BranchImageResponseDTO;
import com.example.sucursal_api.image.dto.BranchImageUpdateDTO;
import com.example.sucursal_api.image.port.in.BranchImageUseCase;
import com.example.sucursal_api.image.port.out.BranchImageRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;


@Service
public class BranchImageService implements BranchImageUseCase {

    private final BranchImageRepository repo;
    private final BranchRepository branchRepo;
    private final BranchImageMapper mapper;
    private final StorageService storage;
    private final String publicPrefix;

    public BranchImageService(BranchImageRepository repo, BranchRepository branchRepo, BranchImageMapper mapper, StorageService storage, @Value("${app.storage.public-prefix:/files/}") String publicPrefix) {
        this.repo = repo;
        this.branchRepo = branchRepo;
        this.mapper = mapper;
        this.storage = storage;
        this.publicPrefix = publicPrefix;
    }

    private void ensureBranchExists(UUID branchId) {
        try { branchRepo.findById(branchId); }
        catch (Exception e) { throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Sucursal no encontrada: " + branchId); }
    }

    private void ensureBelongsToBranch(BranchImage img, UUID branchId) {
        if (!img.getBranchId().equals(branchId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La imagen no pertenece a la sucursal especificada.");
        }
    }

    @Override
    @Transactional
    public BranchImageResponseDTO add(UUID branchId, BranchImageRequestDTO dto) {
        ensureBranchExists(branchId);
        boolean cover = dto.cover() != null && dto.cover();
        if (cover) repo.clearCoverByBranch(branchId);

        BranchImage img = new BranchImage(null, branchId, dto.url(), dto.title(), dto.altText(), cover);
        return mapper.toResponseDTO(repo.save(img));
    }

    @Override
    @Transactional(readOnly = true)
    public BranchImageResponseDTO get(UUID branchId, UUID imageId) {
        ensureBranchExists(branchId);
        BranchImage img = repo.findById(imageId);
        ensureBelongsToBranch(img, branchId);
        return mapper.toResponseDTO(img);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BranchImageResponseDTO> list(UUID branchId) {
        ensureBranchExists(branchId);
        return repo.findByBranch(branchId).stream().map(mapper::toResponseDTO).toList();
    }

    @Override
    @Transactional
    public BranchImageResponseDTO update(UUID branchId, UUID imageId, BranchImageUpdateDTO dto) {
        ensureBranchExists(branchId);
        BranchImage current = repo.findById(imageId);
        ensureBelongsToBranch(current, branchId);

        if (dto.url() != null) current.setUrl(dto.url());
        if (dto.title() != null) current.setTitle(dto.title());
        if (dto.altText() != null) current.setAltText(dto.altText());

        if (dto.cover() != null) {
            if (dto.cover()) {
                repo.clearCoverByBranch(branchId);
                current.setCover(true);
            } else {
                current.setCover(false);
            }
        }

        return mapper.toResponseDTO(repo.save(current));
    }

    @Override
    @Transactional
    public void delete(UUID branchId, UUID imageId) {
        ensureBranchExists(branchId);
        BranchImage current = repo.findById(imageId);
        ensureBelongsToBranch(current, branchId);

        String url = current.getUrl();
        if (url != null && url.startsWith(publicPrefix)) {
            String relativePath = url.substring(publicPrefix.length());
            storage.delete(relativePath);
        }

        repo.delete(imageId);
    }

    @Override
    @Transactional
    public BranchImageResponseDTO setCover(UUID branchId, UUID imageId) {
        ensureBranchExists(branchId);
        BranchImage img = repo.findById(imageId);
        ensureBelongsToBranch(img, branchId);
        repo.clearCoverByBranch(branchId);
        img.setCover(true);
        return mapper.toResponseDTO(repo.save(img));
    }
}
