package com.example.sucursal_api.employee.port.out;


import com.example.sucursal_api.employee.domain.Employee;

import java.util.List;
import java.util.UUID;

public interface EmployeeRepository {
    Employee save(Employee employee);
    Employee findById(UUID id);
    List<Employee> findAll();
    void delete(UUID id);

    boolean existsByInstitutionalEmail(String institutionalEmail);

    default boolean existsByDocNumber(String docNumber) { return false; }
}
