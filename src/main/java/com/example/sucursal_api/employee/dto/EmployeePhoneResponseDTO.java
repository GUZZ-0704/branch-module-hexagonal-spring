package com.example.sucursal_api.employee.dto;

import java.util.UUID;

public record EmployeePhoneResponseDTO(
        UUID id,
        UUID employeeId,
        String number,
        String label,
        boolean whatsapp,
        boolean primary
) {}
