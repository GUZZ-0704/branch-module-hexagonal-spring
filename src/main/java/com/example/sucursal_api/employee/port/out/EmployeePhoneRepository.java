package com.example.sucursal_api.employee.port.out;


import com.example.sucursal_api.employee.domain.EmployeePhone;

import java.util.List;
import java.util.UUID;

public interface EmployeePhoneRepository {
    EmployeePhone save(EmployeePhone phone);
    EmployeePhone findById(UUID id);
    List<EmployeePhone> findByEmployee(UUID employeeId);
    void delete(UUID id);

    boolean existsByEmployeeAndNumber(UUID employeeId, String number);

    default boolean existsById(UUID id) { return false; }
}
