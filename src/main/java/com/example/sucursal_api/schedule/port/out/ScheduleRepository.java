package com.example.sucursal_api.schedule.port.out;


import com.example.sucursal_api.schedule.domain.Schedule;

import java.util.List;
import java.util.UUID;

public interface ScheduleRepository {
    Schedule save(Schedule schedule);

    Schedule findByBranchAndDay(UUID branchId, int dayOfWeek);

    List<Schedule> findByBranch(UUID branchId);

    void deleteByBranchAndDay(UUID branchId, int dayOfWeek);

    boolean existsByBranchAndDay(UUID branchId, int dayOfWeek);
}
