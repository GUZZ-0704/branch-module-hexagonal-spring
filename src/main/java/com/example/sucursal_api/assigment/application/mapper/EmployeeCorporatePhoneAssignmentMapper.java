package com.example.sucursal_api.assigment.application.mapper;

import com.example.sucursal_api.assigment.adapter.out.EmployeeCorporatePhoneAssignmentEntity;
import com.example.sucursal_api.assigment.domain.EmployeeCorporatePhoneAssignment;
import com.example.sucursal_api.assigment.dto.CorpPhoneAssignmentResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EmployeeCorporatePhoneAssignmentMapper {

    @Mapping(target = "employeeId",     source = "employee.id")
    @Mapping(target = "branchId",       source = "branch.id")
    @Mapping(target = "branchPhoneId",  source = "branchPhone.id")
    EmployeeCorporatePhoneAssignment entityToDomain(EmployeeCorporatePhoneAssignmentEntity entity);

    @Mapping(target = "employee",    ignore = true)
    @Mapping(target = "branch",      ignore = true)
    @Mapping(target = "branchPhone", ignore = true)
    EmployeeCorporatePhoneAssignmentEntity domainToEntity(EmployeeCorporatePhoneAssignment domain);

    @Mapping(target = "active", expression = "java(domain.isActive())")
    CorpPhoneAssignmentResponseDTO toResponseDTO(EmployeeCorporatePhoneAssignment domain);

    @Mapping(target = "employeeId",     source = "employee.id")
    @Mapping(target = "branchId",       source = "branch.id")
    @Mapping(target = "branchPhoneId",  source = "branchPhone.id")
    List<EmployeeCorporatePhoneAssignment> entitiesToDomains(List<EmployeeCorporatePhoneAssignmentEntity> entities);
}
