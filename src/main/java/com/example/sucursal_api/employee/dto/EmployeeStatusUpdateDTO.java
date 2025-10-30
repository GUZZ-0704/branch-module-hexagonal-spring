package com.example.sucursal_api.employee.dto;


import com.example.sucursal_api.employee.domain.EmployeeStatus;
import jakarta.validation.constraints.NotNull;

public record EmployeeStatusUpdateDTO(
        @NotNull EmployeeStatus status
) {}

