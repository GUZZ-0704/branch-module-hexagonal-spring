package com.example.sucursal_api.assigment.adapter.in;

import com.example.sucursal_api.assigment.dto.AssignCorpPhoneRequestDTO;
import com.example.sucursal_api.assigment.dto.CloseAssignmentRequestDTO;
import com.example.sucursal_api.assigment.dto.CorpPhoneAssignmentResponseDTO;
import com.example.sucursal_api.assigment.port.in.AssignCorpPhoneUseCase;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@RestController
public class AssignCorpPhoneController {

    private final AssignCorpPhoneUseCase useCase;

    public AssignCorpPhoneController(AssignCorpPhoneUseCase useCase) {
        this.useCase = useCase;
    }

    @PostMapping("/api/employees/{employeeId}/assignments/corporate-phones/{branchPhoneId}")
    public ResponseEntity<CorpPhoneAssignmentResponseDTO> assign(
            @PathVariable UUID employeeId,
            @PathVariable UUID branchPhoneId,
            @Valid @RequestBody(required = false) AssignCorpPhoneRequestDTO dto) {
        return ResponseEntity.ok(useCase.assign(employeeId, branchPhoneId, dto == null ? new AssignCorpPhoneRequestDTO(null) : dto));
    }

    @PatchMapping("/api/assignments/corporate-phones/{corpAssignId}/close")
    public ResponseEntity<CorpPhoneAssignmentResponseDTO> close(
            @PathVariable UUID corpAssignId,
            @Valid @RequestBody(required = false) CloseAssignmentRequestDTO dto) {
        return ResponseEntity.ok(useCase.close(corpAssignId, dto == null ? new CloseAssignmentRequestDTO(null) : dto));
    }

    @GetMapping("/api/employees/{employeeId}/assignments/corporate-phones/active")
    public ResponseEntity<CorpPhoneAssignmentResponseDTO> getActiveByEmployee(@PathVariable UUID employeeId) {
        return ResponseEntity.ok(useCase.getActiveByEmployee(employeeId));
    }

    @GetMapping("/api/phones/{branchPhoneId}/assignment/active")
    public ResponseEntity<CorpPhoneAssignmentResponseDTO> getActiveByPhone(@PathVariable UUID branchPhoneId) {
        try {
            return ResponseEntity.ok(useCase.getActiveByPhone(branchPhoneId));
        } catch (ResponseStatusException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                return ResponseEntity.noContent().build();
            }
            throw e;
        }
    }
}

