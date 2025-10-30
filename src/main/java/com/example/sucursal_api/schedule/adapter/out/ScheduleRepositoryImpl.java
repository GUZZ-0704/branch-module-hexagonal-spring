package com.example.sucursal_api.schedule.adapter.out;

import com.example.sucursal_api.branch.adapter.out.BranchEntity;
import com.example.sucursal_api.schedule.application.mapper.ScheduleMapper;
import com.example.sucursal_api.schedule.domain.Schedule;
import com.example.sucursal_api.schedule.port.out.ScheduleRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Repository
public class ScheduleRepositoryImpl implements ScheduleRepository {

    private final ScheduleJpaRepository jpa;
    private final ScheduleMapper mapper;

    @PersistenceContext
    private final EntityManager entityManager;

    public ScheduleRepositoryImpl(ScheduleJpaRepository jpa, ScheduleMapper mapper, EntityManager entityManager) {
        this.jpa = jpa;
        this.mapper = mapper;
        this.entityManager = entityManager;
    }

    @Override
    public Schedule save(Schedule schedule) {
        ScheduleEntity entity = mapper.domainToEntity(schedule);
        entity.setBranch(entityManager.getReference(BranchEntity.class, schedule.getBranchId()));
        ScheduleEntity saved = jpa.save(entity);
        return mapper.entityToDomain(saved);
    }

    @Override
    public Schedule findByBranchAndDay(UUID branchId, int dayOfWeek) {
        return jpa.findByBranchIdAndDayOfWeek(branchId, dayOfWeek)
                .map(mapper::entityToDomain)
                .orElseThrow(() -> new NoSuchElementException(
                        "Horario no encontrado para branch=" + branchId + " day=" + dayOfWeek));
    }

    @Override
    public List<Schedule> findByBranch(UUID branchId) {
        return jpa.findByBranchIdOrderByDayOfWeekAsc(branchId)
                .stream()
                .map(mapper::entityToDomain)
                .toList();
    }

    @Override
    public void deleteByBranchAndDay(UUID branchId, int dayOfWeek) {
        if (!jpa.existsByBranchIdAndDayOfWeek(branchId, dayOfWeek)) {
            throw new NoSuchElementException(
                    "No existe horario para branch=" + branchId + " day=" + dayOfWeek);
        }
        jpa.deleteByBranchIdAndDayOfWeek(branchId, dayOfWeek);
    }

    @Override
    public boolean existsByBranchAndDay(UUID branchId, int dayOfWeek) {
        return jpa.existsByBranchIdAndDayOfWeek(branchId, dayOfWeek);
    }
}
