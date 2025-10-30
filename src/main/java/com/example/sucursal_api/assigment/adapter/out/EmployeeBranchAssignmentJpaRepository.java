package com.example.sucursal_api.assigment.adapter.out;


import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EmployeeBranchAssignmentJpaRepository extends JpaRepository<EmployeeBranchAssignmentEntity, UUID> {

    Optional<EmployeeBranchAssignmentEntity> findFirstByEmployeeIdAndEndDateIsNull(UUID employeeId);

    List<EmployeeBranchAssignmentEntity> findByBranchIdAndEndDateIsNull(UUID branchId);

    List<EmployeeBranchAssignmentEntity> findByEmployeeIdOrderByStartDateDesc(UUID employeeId);
}
