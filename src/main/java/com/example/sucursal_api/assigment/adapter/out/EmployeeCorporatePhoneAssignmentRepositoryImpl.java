package com.example.sucursal_api.assigment.adapter.out;


import com.example.sucursal_api.assigment.application.mapper.EmployeeCorporatePhoneAssignmentMapper;
import com.example.sucursal_api.assigment.domain.EmployeeCorporatePhoneAssignment;
import com.example.sucursal_api.assigment.port.out.EmployeeCorporatePhoneAssignmentRepository;
import com.example.sucursal_api.branch.adapter.out.BranchEntity;
import com.example.sucursal_api.employee.adapter.out.EmployeeEntity;
import com.example.sucursal_api.phone.adapter.out.BranchPhoneEntity;
import com.example.sucursal_api.phone.adapter.out.BranchPhoneJpaRepository;
import com.example.sucursal_api.phone.domain.PhoneState;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.NoSuchElementException;
import java.util.UUID;

@Repository
public class EmployeeCorporatePhoneAssignmentRepositoryImpl implements EmployeeCorporatePhoneAssignmentRepository {

    private final EmployeeCorporatePhoneAssignmentJpaRepository jpa;
    private final EmployeeCorporatePhoneAssignmentMapper mapper;
    private final BranchPhoneJpaRepository phoneJpa;

    @PersistenceContext
    private EntityManager entityManager;

    public EmployeeCorporatePhoneAssignmentRepositoryImpl(EmployeeCorporatePhoneAssignmentJpaRepository jpa,
                                                          EmployeeCorporatePhoneAssignmentMapper mapper,
                                                          BranchPhoneJpaRepository phoneJpa) {
        this.jpa = jpa;
        this.mapper = mapper;
        this.phoneJpa = phoneJpa;
    }

    @Override
    public EmployeeCorporatePhoneAssignment save(EmployeeCorporatePhoneAssignment a) {
        EmployeeCorporatePhoneAssignmentEntity e = mapper.domainToEntity(a);
        e.setEmployee(entityManager.getReference(EmployeeEntity.class, a.getEmployeeId()));
        e.setBranch(entityManager.getReference(BranchEntity.class, a.getBranchId()));
        e.setBranchPhone(entityManager.getReference(BranchPhoneEntity.class, a.getBranchPhoneId()));
        EmployeeCorporatePhoneAssignmentEntity saved = jpa.save(e);
        return mapper.entityToDomain(saved);
    }

    @Override
    public EmployeeCorporatePhoneAssignment findById(UUID id) {
        return jpa.findById(id)
                .map(mapper::entityToDomain)
                .orElseThrow(() -> new NoSuchElementException("Asignación de corporativo no encontrada: " + id));
    }

    @Override
    public EmployeeCorporatePhoneAssignment findActiveByEmployee(UUID employeeId) {
        return jpa.findFirstByEmployeeIdAndEndDateIsNull(employeeId)
                .map(mapper::entityToDomain)
                .orElse(null);
    }

    @Override
    public EmployeeCorporatePhoneAssignment findActiveByPhone(UUID branchPhoneId) {
        return jpa.findFirstByBranchPhoneIdAndEndDateIsNull(branchPhoneId)
                .map(mapper::entityToDomain)
                .orElse(null);
    }

    @Override
    public void closeActiveByEmployee(UUID employeeId) {
        var active = jpa.findFirstByEmployeeIdAndEndDateIsNull(employeeId).orElse(null);
        if (active != null) {
            active.setEndDate(LocalDate.now());
            jpa.save(active);
            // Liberar el teléfono (cambiar estado a AVAILABLE)
            phoneJpa.findById(active.getBranchPhone().getId()).ifPresent(phone -> {
                phone.setState(PhoneState.AVAILABLE);
                phoneJpa.save(phone);
            });
        }
    }

    @Override
    public void closeActiveByPhone(UUID branchPhoneId) {
        var active = jpa.findFirstByBranchPhoneIdAndEndDateIsNull(branchPhoneId).orElse(null);
        if (active != null) {
            active.setEndDate(LocalDate.now());
            jpa.save(active);
            // Liberar el teléfono (cambiar estado a AVAILABLE)
            phoneJpa.findById(branchPhoneId).ifPresent(phone -> {
                phone.setState(PhoneState.AVAILABLE);
                phoneJpa.save(phone);
            });
        }
    }
    
    @Override
    public void deleteByPhone(UUID branchPhoneId) {
        // Eliminar todas las asignaciones (activas e históricas) asociadas a este teléfono
        jpa.deleteAllByBranchPhoneId(branchPhoneId);
    }
}

