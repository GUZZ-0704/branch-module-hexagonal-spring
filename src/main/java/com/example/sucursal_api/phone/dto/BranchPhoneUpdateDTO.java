package com.example.sucursal_api.phone.dto;

import com.example.sucursal_api.phone.domain.PhoneKind;
import com.example.sucursal_api.phone.domain.PhoneState;
import jakarta.validation.constraints.*;

public record BranchPhoneUpdateDTO(
        @Pattern(regexp = "^[+\\d()\\-\\s]{5,32}$",
                message = "Formato de número inválido")
        String number,

        PhoneKind kind,

        PhoneState state,

        @Size(max = 100)
        String label,

        Boolean whatsapp,
        Boolean publish,

        @Min(0)
        Integer priority
) {}
