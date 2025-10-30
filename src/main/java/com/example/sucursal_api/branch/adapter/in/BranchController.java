package com.example.sucursal_api.branch.adapter.in;


import com.example.sucursal_api.branch.dto.BranchRequestDTO;
import com.example.sucursal_api.branch.dto.BranchResponseDTO;
import com.example.sucursal_api.branch.dto.BranchStatusUpdateDTO;
import com.example.sucursal_api.branch.dto.BranchUpdateDTO;
import com.example.sucursal_api.branch.port.in.BranchUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/branches")
public class BranchController {

    private final BranchUseCase useCase;

    public BranchController(BranchUseCase useCase) {
        this.useCase = useCase;
    }

    @PostMapping
    public ResponseEntity<BranchResponseDTO> create(@Valid @RequestBody BranchRequestDTO dto) {
        BranchResponseDTO created = useCase.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public ResponseEntity<List<BranchResponseDTO>> list() {
        return ResponseEntity.ok(useCase.list());
    }

    @GetMapping("/{branchId}")
    public ResponseEntity<BranchResponseDTO> getById(@PathVariable UUID branchId) {
        return ResponseEntity.ok(useCase.getById(branchId));
    }

    @GetMapping("/slug/{slug}")
    public ResponseEntity<BranchResponseDTO> getBySlug(@PathVariable String slug) {
        return ResponseEntity.ok(useCase.getBySlug(slug));
    }

    @PutMapping("/{branchId}")
    public ResponseEntity<BranchResponseDTO> update(
            @PathVariable UUID branchId,
            @Valid @RequestBody BranchUpdateDTO dto) {
        return ResponseEntity.ok(useCase.update(branchId, dto));
    }

    @PatchMapping("/{branchId}/status")
    public ResponseEntity<BranchResponseDTO> updateStatus(
            @PathVariable UUID branchId,
            @Valid @RequestBody BranchStatusUpdateDTO dto) {
        return ResponseEntity.ok(useCase.updateStatus(branchId, dto));
    }

    @DeleteMapping("/{branchId}")
    public ResponseEntity<Void> delete(@PathVariable UUID branchId) {
        useCase.delete(branchId);
        return ResponseEntity.noContent().build();
    }
}