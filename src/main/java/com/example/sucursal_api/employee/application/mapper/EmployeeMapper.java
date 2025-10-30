package com.example.sucursal_api.employee.application.mapper;


import com.example.sucursal_api.employee.adapter.out.EmployeeEntity;
import com.example.sucursal_api.employee.domain.Employee;
import com.example.sucursal_api.employee.dto.EmployeeResponseDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {
    Employee entityToDomain(EmployeeEntity entity);
    EmployeeEntity domainToEntity(Employee domain);

    EmployeeResponseDTO toResponseDTO(Employee domain);

    List<Employee> entitiesToDomains(List<EmployeeEntity> entities);
}
