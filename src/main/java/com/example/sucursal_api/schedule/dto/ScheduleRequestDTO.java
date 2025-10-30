package com.example.sucursal_api.schedule.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;

public record ScheduleRequestDTO(
        @NotNull @Min(0) @Max(6)
        Integer dayOfWeek,

        @NotNull
        Boolean closed,

        LocalTime open,
        LocalTime close
) {}