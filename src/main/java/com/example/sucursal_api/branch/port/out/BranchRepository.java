package com.example.sucursal_api.branch.port.out;

import com.example.sucursal_api.branch.domain.Branch;

import java.util.List;
import java.util.UUID;

public interface BranchRepository {
    Branch save(Branch branch);
    Branch findById(UUID id);
    Branch findBySlug(String slug);
    List<Branch> findAll();
    boolean existsBySlug(String slug);
    boolean existsBySlugAndIdNot(String slug, UUID excludeId);
    void delete(UUID id);
}