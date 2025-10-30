package com.example.sucursal_api.assigment.dto;

import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public record AssignBranchRequestDTO(
        LocalDate startDate,
        @Size(max = 80) String position,
        @Size(max = 200) String notes
) {}

