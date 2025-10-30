package com.example.sucursal_api.employee.dto;

import jakarta.validation.constraints.*;

public record EmployeePhoneUpdateDTO(
        @Pattern(regexp = "^[+\\d()\\-\\s]{5,32}$", message = "Formato de número inválido")
        String number,

        @Size(max = 100)
        String label,

        Boolean whatsapp,
        Boolean primary
) {}
