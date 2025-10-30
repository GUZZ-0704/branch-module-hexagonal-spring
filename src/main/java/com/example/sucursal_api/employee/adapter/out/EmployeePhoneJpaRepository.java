package com.example.sucursal_api.employee.adapter.out;


import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface EmployeePhoneJpaRepository extends JpaRepository<EmployeePhoneEntity, UUID> {
    List<EmployeePhoneEntity> findByEmployeeId(UUID employeeId);
    boolean existsByEmployeeIdAndNumber(UUID employeeId, String number);
}
