package com.example.sucursal_api.image.port.in;


import com.example.sucursal_api.image.dto.BranchImageRequestDTO;
import com.example.sucursal_api.image.dto.BranchImageResponseDTO;
import com.example.sucursal_api.image.dto.BranchImageUpdateDTO;

import java.util.List;
import java.util.UUID;

public interface BranchImageUseCase {
    BranchImageResponseDTO add(UUID branchId, BranchImageRequestDTO dto);

    BranchImageResponseDTO get(UUID branchId, UUID imageId);

    List<BranchImageResponseDTO> list(UUID branchId);

    BranchImageResponseDTO update(UUID branchId, UUID imageId, BranchImageUpdateDTO dto);

    void delete(UUID branchId, UUID imageId);

    BranchImageResponseDTO setCover(UUID branchId, UUID imageId);
}
