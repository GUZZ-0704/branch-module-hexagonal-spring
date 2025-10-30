package com.example.sucursal_api.image.adapter.in;

import com.example.sucursal_api.image.dto.BranchImageRequestDTO;
import com.example.sucursal_api.image.dto.BranchImageResponseDTO;
import com.example.sucursal_api.image.dto.BranchImageUpdateDTO;
import com.example.sucursal_api.image.port.in.BranchImageUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/branches/{branchId}/images")
public class BranchImageController {

    private final BranchImageUseCase useCase;

    public BranchImageController(BranchImageUseCase useCase) {
        this.useCase = useCase;
    }

    @PostMapping
    public ResponseEntity<BranchImageResponseDTO> add(
            @PathVariable UUID branchId,
            @Valid @RequestBody BranchImageRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(useCase.add(branchId, dto));
    }

    @GetMapping
    public ResponseEntity<List<BranchImageResponseDTO>> list(@PathVariable UUID branchId) {
        return ResponseEntity.ok(useCase.list(branchId));
    }

    @GetMapping("/{imageId}")
    public ResponseEntity<BranchImageResponseDTO> get(
            @PathVariable UUID branchId,
            @PathVariable UUID imageId) {
        return ResponseEntity.ok(useCase.get(branchId, imageId));
    }

    @PutMapping("/{imageId}")
    public ResponseEntity<BranchImageResponseDTO> update(
            @PathVariable UUID branchId,
            @PathVariable UUID imageId,
            @Valid @RequestBody BranchImageUpdateDTO dto) {
        return ResponseEntity.ok(useCase.update(branchId, imageId, dto));
    }

    @DeleteMapping("/{imageId}")
    public ResponseEntity<Void> delete(
            @PathVariable UUID branchId,
            @PathVariable UUID imageId) {
        useCase.delete(branchId, imageId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{imageId}/cover")
    public ResponseEntity<BranchImageResponseDTO> setCover(
            @PathVariable UUID branchId,
            @PathVariable UUID imageId) {
        return ResponseEntity.ok(useCase.setCover(branchId, imageId));
    }
}
