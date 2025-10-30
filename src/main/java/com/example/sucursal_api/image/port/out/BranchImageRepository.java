package com.example.sucursal_api.image.port.out;


import com.example.sucursal_api.image.domain.BranchImage;

import java.util.List;
import java.util.UUID;

public interface BranchImageRepository {
    BranchImage save(BranchImage image);

    BranchImage findById(UUID id);

    List<BranchImage> findByBranch(UUID branchId);

    void delete(UUID id);

    boolean existsById(UUID id);

    BranchImage findCoverByBranch(UUID branchId);

    void clearCoverByBranch(UUID branchId);
}
