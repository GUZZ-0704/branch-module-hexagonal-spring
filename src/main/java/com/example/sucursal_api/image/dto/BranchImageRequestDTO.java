package com.example.sucursal_api.image.dto;


import jakarta.validation.constraints.*;

public record BranchImageRequestDTO(
        @NotBlank(message = "La URL de la imagen es obligatoria")
        @Size(max = 2048, message = "La URL es demasiado larga")
        String url,

        @Size(max = 120, message = "El título no debe superar 120 caracteres")
        String title,

        @Size(max = 180, message = "El texto alternativo no debe superar 180 caracteres")
        String altText,

        Boolean cover
) {}
