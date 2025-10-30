package com.example.sucursal_api.employee.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public record EmployeeUpdateDTO(
        String firstName,
        String lastName,
        String docType,

        @Size(max = 50, message = "El número de documento es demasiado largo")
        String docNumber,

        @Email(message = "Email personal inválido")
        String personalEmail,

        @PastOrPresent(message = "La fecha de ingreso no puede ser futura")
        LocalDate hireDate,

        @PastOrPresent(message = "La fecha de egreso no puede ser futura")
        LocalDate terminationDate
) {}
