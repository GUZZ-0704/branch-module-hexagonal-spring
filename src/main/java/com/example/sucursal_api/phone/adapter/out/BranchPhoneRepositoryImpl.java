package com.example.sucursal_api.phone.adapter.out;

import com.example.sucursal_api.branch.adapter.out.BranchEntity;
import com.example.sucursal_api.phone.application.mapper.BranchPhoneMapper;
import com.example.sucursal_api.phone.domain.BranchPhone;
import com.example.sucursal_api.phone.domain.PhoneKind;
import com.example.sucursal_api.phone.port.out.BranchPhoneRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Repository
public class BranchPhoneRepositoryImpl implements BranchPhoneRepository {

    private final BranchPhoneJpaRepository jpa;
    private final BranchPhoneMapper mapper;

    @PersistenceContext
    private final EntityManager entityManager;

    public BranchPhoneRepositoryImpl(BranchPhoneJpaRepository jpa,
                                     BranchPhoneMapper mapper,
                                     EntityManager entityManager) {
        this.jpa = jpa;
        this.mapper = mapper;
        this.entityManager = entityManager;
    }

    @Override
    public BranchPhone save(BranchPhone phone) {
        BranchPhoneEntity entity = mapper.domainToEntity(phone);
        entity.setBranch(entityManager.getReference(BranchEntity.class, phone.getBranchId()));
        BranchPhoneEntity saved = jpa.save(entity);
        return mapper.entityToDomain(saved);
    }

    @Override
    public BranchPhone findById(UUID id) {
        return jpa.findById(id)
                .map(mapper::entityToDomain)
                .orElseThrow(() -> new NoSuchElementException("Teléfono no encontrado: " + id));
    }

    @Override
    public List<BranchPhone> findByBranch(UUID branchId) {
        return jpa.findByBranchIdOrderByPriorityAsc(branchId)
                .stream().map(mapper::entityToDomain).toList();
    }

    @Override
    public List<BranchPhone> findByBranchAndKind(UUID branchId, PhoneKind kind) {
        return jpa.findByBranchIdAndKindOrderByPriorityAsc(branchId, kind)
                .stream().map(mapper::entityToDomain).toList();
    }

    @Override
    public void delete(UUID id) {
        if (!jpa.existsById(id)) {
            throw new NoSuchElementException("No se encontró el teléfono con id: " + id);
        }
        jpa.deleteById(id);
    }

    @Override
    public boolean existsById(UUID id) {
        return jpa.existsById(id);
    }

    @Override
    public boolean existsByBranchAndNumber(UUID branchId, String number) {
        return jpa.existsByBranchIdAndNumber(branchId, number);
    }
}
