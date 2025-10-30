package com.example.sucursal_api.phone.adapter.in;

import com.example.sucursal_api.phone.domain.PhoneKind;
import com.example.sucursal_api.phone.dto.BranchPhoneRequestDTO;
import com.example.sucursal_api.phone.dto.BranchPhoneResponseDTO;
import com.example.sucursal_api.phone.dto.BranchPhoneUpdateDTO;
import com.example.sucursal_api.phone.port.in.BranchPhoneUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/branches/{branchId}/phones")
public class BranchPhoneController {

    private final BranchPhoneUseCase useCase;

    public BranchPhoneController(BranchPhoneUseCase useCase) {
        this.useCase = useCase;
    }

    @PostMapping
    public ResponseEntity<BranchPhoneResponseDTO> create(
            @PathVariable UUID branchId,
            @Valid @RequestBody BranchPhoneRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(useCase.create(branchId, dto));
    }

    @GetMapping
    public ResponseEntity<List<BranchPhoneResponseDTO>> list(
            @PathVariable UUID branchId,
            @RequestParam(value = "kind", required = false) PhoneKind kind) {
        return ResponseEntity.ok(useCase.listByBranch(branchId, kind));
    }

    @GetMapping("/{phoneId}")
    public ResponseEntity<BranchPhoneResponseDTO> getById(
            @PathVariable UUID branchId,
            @PathVariable UUID phoneId) {
        return ResponseEntity.ok(useCase.getById(branchId, phoneId));
    }

    @PutMapping("/{phoneId}")
    public ResponseEntity<BranchPhoneResponseDTO> update(
            @PathVariable UUID branchId,
            @PathVariable UUID phoneId,
            @Valid @RequestBody BranchPhoneUpdateDTO dto) {
        return ResponseEntity.ok(useCase.update(branchId, phoneId, dto));
    }

    @DeleteMapping("/{phoneId}")
    public ResponseEntity<Void> delete(
            @PathVariable UUID branchId,
            @PathVariable UUID phoneId) {
        useCase.delete(branchId, phoneId);
        return ResponseEntity.noContent().build();
    }
}

