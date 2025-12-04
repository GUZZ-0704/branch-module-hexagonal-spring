package com.example.sucursal_api.assigment.adapter.out;


import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface EmployeeCorporatePhoneAssignmentJpaRepository extends JpaRepository<EmployeeCorporatePhoneAssignmentEntity, UUID> {

    Optional<EmployeeCorporatePhoneAssignmentEntity> findFirstByEmployeeIdAndEndDateIsNull(UUID employeeId);

    Optional<EmployeeCorporatePhoneAssignmentEntity> findFirstByBranchPhoneIdAndEndDateIsNull(UUID branchPhoneId);
    
    void deleteAllByBranchPhoneId(UUID branchPhoneId);
}
