package com.example.sucursal_api.assigment.port.out;


import com.example.sucursal_api.assigment.domain.EmployeeCorporatePhoneAssignment;

import java.util.UUID;

public interface EmployeeCorporatePhoneAssignmentRepository {
    EmployeeCorporatePhoneAssignment save(EmployeeCorporatePhoneAssignment a);
    EmployeeCorporatePhoneAssignment findById(UUID id);

    EmployeeCorporatePhoneAssignment findActiveByEmployee(UUID employeeId);

    EmployeeCorporatePhoneAssignment findActiveByPhone(UUID branchPhoneId);

    void closeActiveByEmployee(UUID employeeId);

    void closeActiveByPhone(UUID branchPhoneId);
    
    void deleteByPhone(UUID branchPhoneId);
}

