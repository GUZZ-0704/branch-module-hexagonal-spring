package com.example.sucursal_api.chatbot.domain;

import java.util.UUID;

public record BranchInfo(
        UUID id,
        String name,
        boolean active
) {}
