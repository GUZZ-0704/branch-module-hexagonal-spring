package com.example.sucursal_api.assigment.adapter.in;

import com.example.sucursal_api.assigment.dto.AssignBranchRequestDTO;
import com.example.sucursal_api.assigment.dto.AssignmentResponseDTO;
import com.example.sucursal_api.assigment.dto.CloseAssignmentRequestDTO;
import com.example.sucursal_api.assigment.port.in.AssignBranchUseCase;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
public class AssignBranchController {

    private final AssignBranchUseCase useCase;

    public AssignBranchController(AssignBranchUseCase useCase) {
        this.useCase = useCase;
    }

    @PostMapping("/api/employees/{employeeId}/assignments/branch/{branchId}")
    public ResponseEntity<AssignmentResponseDTO> assign(
            @PathVariable UUID employeeId,
            @PathVariable UUID branchId,
            @Valid @RequestBody(required = false) AssignBranchRequestDTO dto) {
        return ResponseEntity.ok(useCase.assign(employeeId, branchId, dto == null ? new AssignBranchRequestDTO(null, null, null) : dto));
    }

    @PostMapping("/api/employees/{employeeId}/assignments/branch/{toBranchId}/reassign")
    public ResponseEntity<AssignmentResponseDTO> reassign(
            @PathVariable UUID employeeId,
            @PathVariable UUID toBranchId,
            @Valid @RequestBody(required = false) AssignBranchRequestDTO dto) {
        return ResponseEntity.ok(useCase.reassign(employeeId, toBranchId, dto == null ? new AssignBranchRequestDTO(null, null, null) : dto));
    }

    @PatchMapping("/api/assignments/branch/{assignmentId}/close")
    public ResponseEntity<AssignmentResponseDTO> close(
            @PathVariable UUID assignmentId,
            @Valid @RequestBody(required = false) CloseAssignmentRequestDTO dto) {
        return ResponseEntity.ok(useCase.close(assignmentId, dto == null ? new CloseAssignmentRequestDTO(null) : dto));
    }

    @PatchMapping("/api/assignments/branch/{assignmentId}")
    public ResponseEntity<AssignmentResponseDTO> updatePatch(
            @PathVariable UUID assignmentId,
            @Valid @RequestBody(required = false) AssignBranchRequestDTO dto) {
        return ResponseEntity.ok(useCase.update(assignmentId, dto == null ? new AssignBranchRequestDTO(null, null, null) : dto));
    }

    @PutMapping("/api/assignments/branch/{assignmentId}")
    public ResponseEntity<AssignmentResponseDTO> updatePut(
            @PathVariable UUID assignmentId,
            @Valid @RequestBody(required = false) AssignBranchRequestDTO dto) {
        return ResponseEntity.ok(useCase.update(assignmentId, dto == null ? new AssignBranchRequestDTO(null, null, null) : dto));
    }

    @GetMapping("/api/employees/{employeeId}/assignments/branch/active")
    public ResponseEntity<AssignmentResponseDTO> getActiveByEmployee(@PathVariable UUID employeeId) {
        var dto = useCase.getActiveByEmployee(employeeId);
        if (dto == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/api/branches/{branchId}/assignments/active")
    public ResponseEntity<List<AssignmentResponseDTO>> listActiveByBranch(@PathVariable UUID branchId) {
        return ResponseEntity.ok(useCase.listActiveByBranch(branchId));
    }

    @GetMapping("/api/employees/{employeeId}/assignments/branch/history")
    public ResponseEntity<List<AssignmentResponseDTO>> listHistoryByEmployee(@PathVariable UUID employeeId) {
        return ResponseEntity.ok(useCase.listHistoryByEmployee(employeeId));
    }
}

