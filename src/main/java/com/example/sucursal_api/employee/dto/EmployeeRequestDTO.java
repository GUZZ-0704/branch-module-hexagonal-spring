package com.example.sucursal_api.employee.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public record EmployeeRequestDTO(
        @NotBlank(message = "El nombre es obligatorio")
        String firstName,

        @NotBlank(message = "Los apellidos son obligatorios")
        String lastName,

        String docType,

        @Size(max = 50, message = "El número de documento es demasiado largo")
        String docNumber,

        @Email(message = "Email personal inválido")
        String personalEmail,

        @PastOrPresent(message = "La fecha de ingreso no puede ser futura")
        LocalDate hireDate
) {}
