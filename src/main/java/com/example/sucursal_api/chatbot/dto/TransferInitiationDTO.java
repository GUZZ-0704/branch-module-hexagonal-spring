package com.example.sucursal_api.chatbot.dto;

public record TransferInitiationDTO(
        String productId,
        String fromBranchId,
        String toBranchId,
        Integer quantity,
        String customerCode
) {}
