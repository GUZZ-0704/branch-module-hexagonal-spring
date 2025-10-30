package com.example.sucursal_api.assigment.port.out;


import com.example.sucursal_api.assigment.domain.EmployeeBranchAssignment;

import java.util.List;
import java.util.UUID;

public interface EmployeeBranchAssignmentRepository {
    EmployeeBranchAssignment save(EmployeeBranchAssignment a);
    EmployeeBranchAssignment findById(UUID id);

    EmployeeBranchAssignment findActiveByEmployee(UUID employeeId);

    List<EmployeeBranchAssignment> findActiveByBranch(UUID branchId);

    List<EmployeeBranchAssignment> findHistoryByEmployee(UUID employeeId);

    void closeActiveByEmployee(UUID employeeId);
}

