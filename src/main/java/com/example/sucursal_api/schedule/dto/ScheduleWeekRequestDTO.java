package com.example.sucursal_api.schedule.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record ScheduleWeekRequestDTO(
        @NotEmpty
        List<@Valid ScheduleRequestDTO> days
) {}

