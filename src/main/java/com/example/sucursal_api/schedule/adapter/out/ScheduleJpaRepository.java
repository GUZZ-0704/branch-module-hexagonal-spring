package com.example.sucursal_api.schedule.adapter.out;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ScheduleJpaRepository extends JpaRepository<ScheduleEntity, UUID> {
    Optional<ScheduleEntity> findByBranchIdAndDayOfWeek(UUID branchId, int dayOfWeek);
    List<ScheduleEntity> findByBranchIdOrderByDayOfWeekAsc(UUID branchId);
    boolean existsByBranchIdAndDayOfWeek(UUID branchId, int dayOfWeek);
    void deleteByBranchIdAndDayOfWeek(UUID branchId, int dayOfWeek);
}
