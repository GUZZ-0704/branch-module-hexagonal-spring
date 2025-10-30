package com.example.sucursal_api.phone.dto;


import com.example.sucursal_api.phone.domain.PhoneKind;
import com.example.sucursal_api.phone.domain.PhoneState;
import jakarta.validation.constraints.*;

public record BranchPhoneRequestDTO(
        @NotBlank(message = "El número es obligatorio")
        @Pattern(regexp = "^[+\\d()\\-\\s]{5,32}$",
                message = "Formato de número inválido")
        String number,

        @NotNull(message = "El tipo (kind) es obligatorio")
        PhoneKind kind,

        PhoneState state,

        @Size(max = 100)
        String label,

        @NotNull
        Boolean whatsapp,

        @NotNull
        Boolean publish,

        @Min(0)
        Integer priority
) {}

