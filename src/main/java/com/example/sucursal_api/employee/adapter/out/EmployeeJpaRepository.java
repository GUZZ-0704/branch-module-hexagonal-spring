package com.example.sucursal_api.employee.adapter.out;


import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EmployeeJpaRepository extends JpaRepository<EmployeeEntity, UUID> {
    boolean existsByInstitutionalEmail(String institutionalEmail);
}
