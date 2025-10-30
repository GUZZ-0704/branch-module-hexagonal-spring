package com.example.sucursal_api.schedule.dto;


import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import java.time.LocalTime;

public record ScheduleUpdateDTO(
        @Min(0) @Max(6)
        Integer dayOfWeek,
        Boolean closed,
        LocalTime open,
        LocalTime close
) {}

