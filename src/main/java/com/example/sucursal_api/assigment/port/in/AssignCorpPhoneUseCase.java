package com.example.sucursal_api.assigment.port.in;


import com.example.sucursal_api.assigment.dto.AssignCorpPhoneRequestDTO;
import com.example.sucursal_api.assigment.dto.CloseAssignmentRequestDTO;
import com.example.sucursal_api.assigment.dto.CorpPhoneAssignmentResponseDTO;

import java.util.UUID;

public interface AssignCorpPhoneUseCase {


    CorpPhoneAssignmentResponseDTO assign(UUID employeeId, UUID branchPhoneId, AssignCorpPhoneRequestDTO dto);

    CorpPhoneAssignmentResponseDTO close(UUID corpPhoneAssignmentId, CloseAssignmentRequestDTO dto);

    CorpPhoneAssignmentResponseDTO getActiveByEmployee(UUID employeeId);

    CorpPhoneAssignmentResponseDTO getActiveByPhone(UUID branchPhoneId);
}
