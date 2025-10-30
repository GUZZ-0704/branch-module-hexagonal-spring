package com.example.sucursal_api.employee.adapter.out;

import com.example.sucursal_api.employee.application.mapper.EmployeePhoneMapper;
import com.example.sucursal_api.employee.domain.EmployeePhone;
import com.example.sucursal_api.employee.port.out.EmployeePhoneRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Repository
public class EmployeePhoneRepositoryImpl implements EmployeePhoneRepository {

    private final EmployeePhoneJpaRepository jpa;
    private final EmployeePhoneMapper mapper;

    @PersistenceContext
    private final EntityManager entityManager;

    public EmployeePhoneRepositoryImpl(EmployeePhoneJpaRepository jpa,
                                       EmployeePhoneMapper mapper,
                                       EntityManager entityManager) {
        this.jpa = jpa;
        this.mapper = mapper;
        this.entityManager = entityManager;
    }

    @Override
    public EmployeePhone save(EmployeePhone phone) {
        EmployeePhoneEntity e = mapper.domainToEntity(phone);
        e.setEmployee(entityManager.getReference(EmployeeEntity.class, phone.getEmployeeId()));
        EmployeePhoneEntity saved = jpa.save(e);
        return mapper.entityToDomain(saved);
    }

    @Override
    public EmployeePhone findById(UUID id) {
        return jpa.findById(id)
                .map(mapper::entityToDomain)
                .orElseThrow(() -> new NoSuchElementException("Teléfono de empleado no encontrado: " + id));
    }

    @Override
    public List<EmployeePhone> findByEmployee(UUID employeeId) {
        return jpa.findByEmployeeId(employeeId).stream()
                .map(mapper::entityToDomain)
                .toList();
    }

    @Override
    public void delete(UUID id) {
        if (!jpa.existsById(id)) {
            throw new NoSuchElementException("No se encontró el teléfono con id: " + id);
        }
        jpa.deleteById(id);
    }

    @Override
    public boolean existsByEmployeeAndNumber(UUID employeeId, String number) {
        return jpa.existsByEmployeeIdAndNumber(employeeId, number);
    }
}
