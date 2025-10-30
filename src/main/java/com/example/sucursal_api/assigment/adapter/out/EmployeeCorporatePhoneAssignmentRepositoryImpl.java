package com.example.sucursal_api.assigment.adapter.out;


import com.example.sucursal_api.assigment.application.mapper.EmployeeCorporatePhoneAssignmentMapper;
import com.example.sucursal_api.assigment.domain.EmployeeCorporatePhoneAssignment;
import com.example.sucursal_api.assigment.port.out.EmployeeCorporatePhoneAssignmentRepository;
import com.example.sucursal_api.branch.adapter.out.BranchEntity;
import com.example.sucursal_api.employee.adapter.out.EmployeeEntity;
import com.example.sucursal_api.phone.adapter.out.BranchPhoneEntity;
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

    @PersistenceContext
    private final EntityManager entityManager;

    public EmployeeCorporatePhoneAssignmentRepositoryImpl(EmployeeCorporatePhoneAssignmentJpaRepository jpa,
                                                          EmployeeCorporatePhoneAssignmentMapper mapper,
                                                          EntityManager entityManager) {
        this.jpa = jpa;
        this.mapper = mapper;
        this.entityManager = entityManager;
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
        }
    }

    @Override
    public void closeActiveByPhone(UUID branchPhoneId) {
        var active = jpa.findFirstByBranchPhoneIdAndEndDateIsNull(branchPhoneId).orElse(null);
        if (active != null) {
            active.setEndDate(LocalDate.now());
            jpa.save(active);
        }
    }
}

