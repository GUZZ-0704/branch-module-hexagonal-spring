package com.example.sucursal_api.phone.port.out;


import com.example.sucursal_api.phone.domain.BranchPhone;
import com.example.sucursal_api.phone.domain.PhoneKind;

import java.util.List;
import java.util.UUID;

public interface BranchPhoneRepository {
    BranchPhone save(BranchPhone phone);

    BranchPhone findById(UUID id);

    List<BranchPhone> findByBranch(UUID branchId);

    List<BranchPhone> findByBranchAndKind(UUID branchId, PhoneKind kind);

    void delete(UUID id);

    boolean existsById(UUID id);

    boolean existsByBranchAndNumber(UUID branchId, String number);
}

