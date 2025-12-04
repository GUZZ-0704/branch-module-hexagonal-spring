package com.example.sucursal_api.chatbot.domain;

import java.util.UUID;

public record TransferRequest(
        UUID productId,
        UUID fromBranchId,
        UUID toBranchId,
        int quantity,
        String reason,
        String requestedBy
) {}
