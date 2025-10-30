package com.example.sucursal_api.assigment.application.mapper;

import com.example.sucursal_api.assigment.adapter.out.EmployeeBranchAssignmentEntity;
import com.example.sucursal_api.assigment.domain.EmployeeBranchAssignment;
import com.example.sucursal_api.assigment.dto.AssignmentResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EmployeeBranchAssignmentMapper {

    @Mapping(target = "employeeId", source = "employee.id")
    @Mapping(target = "branchId",   source = "branch.id")
    EmployeeBranchAssignment entityToDomain(EmployeeBranchAssignmentEntity entity);

    @Mapping(target = "employee", ignore = true)
    @Mapping(target = "branch",   ignore = true)
    EmployeeBranchAssignmentEntity domainToEntity(EmployeeBranchAssignment domain);

    @Mapping(target = "active", expression = "java(domain.isActive())")
    AssignmentResponseDTO toResponseDTO(EmployeeBranchAssignment domain);

    @Mapping(target = "employeeId", source = "employee.id")
    @Mapping(target = "branchId",   source = "branch.id")
    List<EmployeeBranchAssignment> entitiesToDomains(List<EmployeeBranchAssignmentEntity> entities);
}
