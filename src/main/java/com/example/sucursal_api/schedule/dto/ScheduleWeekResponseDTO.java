package com.example.sucursal_api.schedule.dto;


import java.util.List;

public record ScheduleWeekResponseDTO(
        List<ScheduleResponseDTO> days
) {}
