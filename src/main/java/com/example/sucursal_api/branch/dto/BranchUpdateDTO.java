package com.example.sucursal_api.branch.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record BranchUpdateDTO(
        String name,

        @Pattern(regexp = "^[a-z0-9-]{3,64}$",
                message = "Slug en minúsculas, números y guiones")
        String slug,

        String address,
        String primaryPhone,

        @DecimalMin("-90.0") @DecimalMax("90.0")
        BigDecimal lat,

        @DecimalMin("-180.0") @DecimalMax("180.0")
        BigDecimal lng
) {}
