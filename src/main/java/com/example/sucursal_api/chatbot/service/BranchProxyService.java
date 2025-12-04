package com.example.sucursal_api.chatbot.service;

import com.example.sucursal_api.branch.application.BranchService;
import com.example.sucursal_api.branch.dto.BranchResponseDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class BranchProxyService {

    private final BranchService branchService;

    public BranchProxyService(BranchService branchService) {
        this.branchService = branchService;
    }

    public List<BranchResponseDTO> listBranches() {
        return branchService.list();
    }

    public BranchResponseDTO getBranchById(UUID id) {
        return branchService.getById(id);
    }
}
