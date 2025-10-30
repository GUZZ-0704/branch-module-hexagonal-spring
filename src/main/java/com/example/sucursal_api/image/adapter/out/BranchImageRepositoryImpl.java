package com.example.sucursal_api.image.adapter.out;

import com.example.sucursal_api.branch.adapter.out.BranchEntity;
import com.example.sucursal_api.image.application.mapper.BranchImageMapper;
import com.example.sucursal_api.image.domain.BranchImage;
import com.example.sucursal_api.image.port.out.BranchImageRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Repository
public class BranchImageRepositoryImpl implements BranchImageRepository {

    private final BranchImageJpaRepository jpa;
    private final BranchImageMapper mapper;

    @PersistenceContext
    private final EntityManager entityManager;

    public BranchImageRepositoryImpl(BranchImageJpaRepository jpa,
                                     BranchImageMapper mapper,
                                     EntityManager entityManager) {
        this.jpa = jpa;
        this.mapper = mapper;
        this.entityManager = entityManager;
    }

    @Override
    public BranchImage save(BranchImage image) {
        BranchImageEntity e = mapper.domainToEntity(image);
        e.setBranch(entityManager.getReference(BranchEntity.class, image.getBranchId()));
        BranchImageEntity saved = jpa.save(e);
        return mapper.entityToDomain(saved);
    }

    @Override
    public BranchImage findById(UUID id) {
        return jpa.findById(id)
                .map(mapper::entityToDomain)
                .orElseThrow(() -> new NoSuchElementException("Imagen no encontrada: " + id));
    }

    @Override
    public List<BranchImage> findByBranch(UUID branchId) {
        return jpa.findByBranchId(branchId).stream()
                .map(mapper::entityToDomain)
                .toList();
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        if (!jpa.existsById(id)) {
            throw new NoSuchElementException("No se encontró la imagen con id: " + id);
        }
        jpa.deleteById(id);
    }

    @Override
    public boolean existsById(UUID id) {
        return jpa.existsById(id);
    }

    @Override
    public BranchImage findCoverByBranch(UUID branchId) {
        return jpa.findFirstByBranchIdAndCoverTrue(branchId)
                .map(mapper::entityToDomain)
                .orElse(null);
    }

    @Override
    @Transactional
    public void clearCoverByBranch(UUID branchId) {
        jpa.clearCoverByBranch(branchId);
    }
}
