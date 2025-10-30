package com.example.sucursal_api.assigment.dto;


import java.time.LocalDate;
import java.util.UUID;

public record AssignmentResponseDTO(
        UUID id,
        UUID employeeId,
        UUID branchId,
        LocalDate startDate,
        LocalDate endDate,
        String position,
        String notes,
        boolean active
) {}
