package com.example.sucursal_api.employee.dto;

import com.example.sucursal_api.employee.domain.EmployeeStatus;

import java.time.LocalDate;
import java.util.UUID;

public record EmployeeResponseDTO(
        UUID id,
        String firstName,
        String lastName,
        String docType,
        String docNumber,
        String personalEmail,
        String institutionalEmail,
        EmployeeStatus status,
        LocalDate hireDate,
        LocalDate terminationDate
) {}

