package com.example.sucursal_api.schedule.port.in;



import com.example.sucursal_api.schedule.dto.*;

import java.util.List;
import java.util.UUID;

public interface ScheduleUseCase {
    ScheduleResponseDTO upsert(UUID branchId, ScheduleRequestDTO dto);

    ScheduleResponseDTO update(UUID branchId, int dayOfWeek, ScheduleUpdateDTO dto);

    ScheduleResponseDTO getDay(UUID branchId, int dayOfWeek);
    List<ScheduleResponseDTO> getWeek(UUID branchId);

    void deleteDay(UUID branchId, int dayOfWeek);

    ScheduleWeekResponseDTO upsertWeek(UUID branchId, ScheduleWeekRequestDTO dto);
}
