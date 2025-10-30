package com.example.sucursal_api.branch.adapter.out;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface BranchJpaRepository extends JpaRepository<BranchEntity, UUID> {
    boolean existsBySlug(String slug);
    boolean existsBySlugAndIdNot(String slug, UUID id);
    Optional<BranchEntity> findBySlug(String slug);
}
