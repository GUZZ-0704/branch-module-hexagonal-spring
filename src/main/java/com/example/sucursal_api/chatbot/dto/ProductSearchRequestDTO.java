package com.example.sucursal_api.chatbot.dto;

import java.util.UUID;

public record ProductSearchRequestDTO(
        String nameOrSku,
        UUID branchId
) {}
