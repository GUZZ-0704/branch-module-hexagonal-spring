package com.example.sucursal_api.branch.adapter.out;

import com.example.sucursal_api.branch.application.mapper.BranchMapper;
import com.example.sucursal_api.branch.domain.Branch;
import com.example.sucursal_api.branch.port.out.BranchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Repository
public class BranchRepositoryImpl implements BranchRepository {

    private final BranchJpaRepository jpa;
    private final BranchMapper mapper;

    public BranchRepositoryImpl(BranchJpaRepository jpa, BranchMapper mapper) {
        this.jpa = jpa;
        this.mapper = mapper;
    }

    @Override
    public Branch save(Branch branch) {
        BranchEntity entity = mapper.domainToEntity(branch);
        BranchEntity saved = jpa.save(entity);
        return mapper.entityToDomain(saved);
    }

    @Override
    public Branch findById(UUID id) {
        return jpa.findById(id)
                .map(mapper::entityToDomain)
                .orElseThrow(() -> new NoSuchElementException("Sucursal no encontrada con id: " + id));
    }

    @Override
    public Branch findBySlug(String slug) {
        return jpa.findBySlug(slug)
                .map(mapper::entityToDomain)
                .orElseThrow(() -> new NoSuchElementException("Sucursal no encontrada con slug: " + slug));
    }

    @Override
    public List<Branch> findAll() {
        return jpa.findAll().stream().map(mapper::entityToDomain).toList();
    }

    @Override
    public boolean existsBySlug(String slug) {
        return jpa.existsBySlug(slug);
    }

    @Override
    public boolean existsBySlugAndIdNot(String slug, UUID excludeId) {
        return jpa.existsBySlugAndIdNot(slug, excludeId);
    }

    @Override
    public void delete(UUID id) {
        if (!jpa.existsById(id)) {
            throw new NoSuchElementException("No se encontró la sucursal con id: " + id);
        }
        jpa.deleteById(id);
    }
}

