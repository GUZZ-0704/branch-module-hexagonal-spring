package com.example.sucursal_api.assigment.port.in;


import com.example.sucursal_api.assigment.dto.AssignBranchRequestDTO;
import com.example.sucursal_api.assigment.dto.AssignmentResponseDTO;
import com.example.sucursal_api.assigment.dto.CloseAssignmentRequestDTO;

import java.util.List;
import java.util.UUID;

public interface AssignBranchUseCase {

    AssignmentResponseDTO assign(UUID employeeId, UUID branchId, AssignBranchRequestDTO dto);

    AssignmentResponseDTO reassign(UUID employeeId, UUID toBranchId, AssignBranchRequestDTO dto);

    AssignmentResponseDTO close(UUID assignmentId, CloseAssignmentRequestDTO dto);

    AssignmentResponseDTO getActiveByEmployee(UUID employeeId);

    List<AssignmentResponseDTO> listActiveByBranch(UUID branchId);

    List<AssignmentResponseDTO> listHistoryByEmployee(UUID employeeId);
}
