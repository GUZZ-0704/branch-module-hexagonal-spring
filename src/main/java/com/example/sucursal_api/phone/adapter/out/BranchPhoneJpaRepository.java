package com.example.sucursal_api.phone.adapter.out;


import com.example.sucursal_api.phone.domain.PhoneKind;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BranchPhoneJpaRepository extends JpaRepository<BranchPhoneEntity, UUID> {
    List<BranchPhoneEntity> findByBranchIdOrderByPriorityAsc(UUID branchId);
    List<BranchPhoneEntity> findByBranchIdAndKindOrderByPriorityAsc(UUID branchId, PhoneKind kind);
    boolean existsByBranchIdAndNumber(UUID branchId, String number);
    Optional<BranchPhoneEntity> findById(UUID id);
}
