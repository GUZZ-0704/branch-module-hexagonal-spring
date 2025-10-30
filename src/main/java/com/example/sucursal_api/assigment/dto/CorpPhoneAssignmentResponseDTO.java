package com.example.sucursal_api.assigment.dto;


import java.time.LocalDate;
import java.util.UUID;

public record CorpPhoneAssignmentResponseDTO(
        UUID id,
        UUID employeeId,
        UUID branchId,
        UUID branchPhoneId,
        LocalDate startDate,
        LocalDate endDate,
        boolean active
) {}
