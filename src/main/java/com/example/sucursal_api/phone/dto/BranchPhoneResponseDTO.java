package com.example.sucursal_api.phone.dto;


import com.example.sucursal_api.phone.domain.PhoneKind;
import com.example.sucursal_api.phone.domain.PhoneState;

import java.util.UUID;

public record BranchPhoneResponseDTO(
        UUID id,
        UUID branchId,
        String number,
        PhoneKind kind,
        PhoneState state,
        String label,
        boolean whatsapp,
        boolean publish,
        int priority
) {}

