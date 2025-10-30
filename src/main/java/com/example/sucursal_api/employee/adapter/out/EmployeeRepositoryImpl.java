package com.example.sucursal_api.employee.adapter.out;


import com.example.sucursal_api.employee.application.mapper.EmployeeMapper;
import com.example.sucursal_api.employee.domain.Employee;
import com.example.sucursal_api.employee.port.out.EmployeeRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Repository
public class EmployeeRepositoryImpl implements EmployeeRepository {

    private final EmployeeJpaRepository jpa;
    private final EmployeeMapper mapper;

    public EmployeeRepositoryImpl(EmployeeJpaRepository jpa, EmployeeMapper mapper) {
        this.jpa = jpa;
        this.mapper = mapper;
    }

    @Override
    public Employee save(Employee employee) {
        EmployeeEntity e = mapper.domainToEntity(employee);
        EmployeeEntity saved = jpa.save(e);
        return mapper.entityToDomain(saved);
    }

    @Override
    public Employee findById(UUID id) {
        return jpa.findById(id)
                .map(mapper::entityToDomain)
                .orElseThrow(() -> new NoSuchElementException("Empleado no encontrado: " + id));
    }

    @Override
    public List<Employee> findAll() {
        return jpa.findAll().stream().map(mapper::entityToDomain).toList();
    }

    @Override
    public void delete(UUID id) {
        if (!jpa.existsById(id)) {
            throw new NoSuchElementException("No se encontró el empleado con id: " + id);
        }
        jpa.deleteById(id);
    }

    @Override
    public boolean existsByInstitutionalEmail(String institutionalEmail) {
        return jpa.existsByInstitutionalEmail(institutionalEmail);
    }
}
