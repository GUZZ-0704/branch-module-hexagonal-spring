package com.example.sucursal_api.assigment.dto;


import jakarta.validation.constraints.PastOrPresent;
import java.time.LocalDate;

public record CloseAssignmentRequestDTO(
        @PastOrPresent(message = "La fecha de cierre no puede ser futura")
        LocalDate endDate
) {}

