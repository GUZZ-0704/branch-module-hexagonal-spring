package com.example.sucursal_api.assigment.adapter.out;


import com.example.sucursal_api.assigment.application.mapper.EmployeeBranchAssignmentMapper;
import com.example.sucursal_api.assigment.domain.EmployeeBranchAssignment;
import com.example.sucursal_api.assigment.port.out.EmployeeBranchAssignmentRepository;
import com.example.sucursal_api.branch.adapter.out.BranchEntity;
import com.example.sucursal_api.employee.adapter.out.EmployeeEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Repository
public class EmployeeBranchAssignmentRepositoryImpl implements EmployeeBranchAssignmentRepository {

    private final EmployeeBranchAssignmentJpaRepository jpa;
    private final EmployeeBranchAssignmentMapper mapper;

    @PersistenceContext
    private final EntityManager entityManager;

    public EmployeeBranchAssignmentRepositoryImpl(EmployeeBranchAssignmentJpaRepository jpa,
                                                  EmployeeBranchAssignmentMapper mapper,
                                                  EntityManager entityManager) {
        this.jpa = jpa;
        this.mapper = mapper;
        this.entityManager = entityManager;
    }

    @Override
    public EmployeeBranchAssignment save(EmployeeBranchAssignment a) {
        EmployeeBranchAssignmentEntity e = mapper.domainToEntity(a);
        e.setEmployee(entityManager.getReference(EmployeeEntity.class, a.getEmployeeId()));
        e.setBranch(entityManager.getReference(BranchEntity.class, a.getBranchId()));
        EmployeeBranchAssignmentEntity saved = jpa.save(e);
        return mapper.entityToDomain(saved);
    }

    @Override
    public EmployeeBranchAssignment findById(UUID id) {
        return jpa.findById(id)
                .map(mapper::entityToDomain)
                .orElseThrow(() -> new NoSuchElementException("Asignación no encontrada: " + id));
    }

    @Override
    public EmployeeBranchAssignment findActiveByEmployee(UUID employeeId) {
        return jpa.findFirstByEmployeeIdAndEndDateIsNull(employeeId)
                .map(mapper::entityToDomain)
                .orElse(null);
    }

    @Override
    public List<EmployeeBranchAssignment> findActiveByBranch(UUID branchId) {
        return jpa.findByBranchIdAndEndDateIsNull(branchId).stream()
                .map(mapper::entityToDomain)
                .toList();
    }

    @Override
    public List<EmployeeBranchAssignment> findHistoryByEmployee(UUID employeeId) {
        return jpa.findByEmployeeIdOrderByStartDateDesc(employeeId).stream()
                .map(mapper::entityToDomain)
                .toList();
    }

    @Override
    public void closeActiveByEmployee(UUID employeeId) {
        var active = jpa.findFirstByEmployeeIdAndEndDateIsNull(employeeId).orElse(null);
        if (active != null) {
            active.setEndDate(LocalDate.now());
            jpa.save(active);
        }
    }
}
