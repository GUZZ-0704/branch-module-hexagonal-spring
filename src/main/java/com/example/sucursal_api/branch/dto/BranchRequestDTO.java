package com.example.sucursal_api.branch.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record BranchRequestDTO(
        @NotBlank(message = "El nombre es obligatorio")
        String name,

        @Pattern(regexp = "^[a-z0-9-]{3,64}$",
                message = "Slug en minúsculas, números y guiones")
        String slug,

        @NotBlank(message = "La dirección es obligatoria")
        String address,

        String primaryPhone,

        @NotNull @DecimalMin("-90.0") @DecimalMax("90.0")
        BigDecimal lat,

        @NotNull @DecimalMin("-180.0") @DecimalMax("180.0")
        BigDecimal lng
) {}