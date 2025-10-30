package com.example.sucursal_api.employee.application.mapper;

import com.example.sucursal_api.employee.adapter.out.EmployeePhoneEntity;
import com.example.sucursal_api.employee.domain.EmployeePhone;
import com.example.sucursal_api.employee.dto.EmployeePhoneResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EmployeePhoneMapper {

    @Mapping(target = "employeeId", source = "employee.id")
    EmployeePhone entityToDomain(EmployeePhoneEntity entity);

    @Mapping(target = "employee", ignore = true)
    EmployeePhoneEntity domainToEntity(EmployeePhone domain);

    EmployeePhoneResponseDTO toResponseDTO(EmployeePhone domain);

    @Mapping(target = "employeeId", source = "employee.id")
    List<EmployeePhone> entitiesToDomains(List<EmployeePhoneEntity> entities);
}
