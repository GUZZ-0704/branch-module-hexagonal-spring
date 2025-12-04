package com.example.sucursal_api.chatbot.dto;

import java.util.List;

public record BranchListResponseDTO(
        int total,
        List<BranchInfoDTO> branches
) {}
