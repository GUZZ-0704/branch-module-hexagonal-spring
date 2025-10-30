package com.example.sucursal_api.image.adapter.out;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BranchImageJpaRepository extends JpaRepository<BranchImageEntity, UUID> {

    List<BranchImageEntity> findByBranchId(UUID branchId);

    Optional<BranchImageEntity> findFirstByBranchIdAndCoverTrue(UUID branchId);

    @Modifying
    @Query("update BranchImageEntity i set i.cover=false where i.branch.id=:branchId and i.cover=true")
    void clearCoverByBranch(@Param("branchId") UUID branchId);
}
