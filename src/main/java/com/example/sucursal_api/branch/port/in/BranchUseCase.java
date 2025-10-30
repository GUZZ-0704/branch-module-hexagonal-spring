package com.example.sucursal_api.branch.port.in;

import com.example.sucursal_api.branch.dto.BranchRequestDTO;
import com.example.sucursal_api.branch.dto.BranchResponseDTO;
import com.example.sucursal_api.branch.dto.BranchStatusUpdateDTO;
import com.example.sucursal_api.branch.dto.BranchUpdateDTO;

import java.util.List;
import java.util.UUID;

public interface BranchUseCase {
    BranchResponseDTO create(BranchRequestDTO dto);
    BranchResponseDTO getById(UUID id);
    BranchResponseDTO getBySlug(String slug);
    List<BranchResponseDTO> list();
    BranchResponseDTO update(UUID id, BranchUpdateDTO dto);
    BranchResponseDTO updateStatus(UUID id, BranchStatusUpdateDTO dto);
    void delete(UUID id);
}
