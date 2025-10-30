package com.example.sucursal_api.schedule.application.mapper;

import com.example.sucursal_api.schedule.adapter.out.ScheduleEntity;
import com.example.sucursal_api.schedule.domain.Schedule;
import com.example.sucursal_api.schedule.dto.ScheduleResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ScheduleMapper {

    @Mapping(target = "branchId", source = "branch.id")
    Schedule entityToDomain(ScheduleEntity entity);

    ScheduleEntity domainToEntity(Schedule domain);

    ScheduleResponseDTO toResponseDTO(Schedule domain);

    List<Schedule> entitiesToDomains(List<ScheduleEntity> entities);
}
