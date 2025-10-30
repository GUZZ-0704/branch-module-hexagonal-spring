package com.example.sucursal_api.phone.port.in;


import com.example.sucursal_api.phone.domain.PhoneKind;
import com.example.sucursal_api.phone.dto.BranchPhoneRequestDTO;
import com.example.sucursal_api.phone.dto.BranchPhoneResponseDTO;
import com.example.sucursal_api.phone.dto.BranchPhoneUpdateDTO;

import java.util.List;
import java.util.UUID;

public interface BranchPhoneUseCase {
    BranchPhoneResponseDTO create(UUID branchId, BranchPhoneRequestDTO dto);

    BranchPhoneResponseDTO getById(UUID branchId, UUID phoneId);

    List<BranchPhoneResponseDTO> listByBranch(UUID branchId, PhoneKind kind);

    BranchPhoneResponseDTO update(UUID branchId, UUID phoneId, BranchPhoneUpdateDTO dto);

    void delete(UUID branchId, UUID phoneId);
}

