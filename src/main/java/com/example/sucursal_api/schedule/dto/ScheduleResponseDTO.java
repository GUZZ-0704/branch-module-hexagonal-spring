package com.example.sucursal_api.schedule.dto;


import java.time.LocalTime;
import java.util.UUID;

public record ScheduleResponseDTO(
        UUID id,
        UUID branchId,
        int dayOfWeek,
        boolean closed,
        LocalTime open,
        LocalTime close
) {}
