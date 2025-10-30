package com.example.sucursal_api.employee.adapter.in;

import com.example.sucursal_api.employee.dto.EmployeeRequestDTO;
import com.example.sucursal_api.employee.dto.EmployeeResponseDTO;
import com.example.sucursal_api.employee.dto.EmployeeStatusUpdateDTO;
import com.example.sucursal_api.employee.dto.EmployeeUpdateDTO;
import com.example.sucursal_api.employee.port.in.EmployeeUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeUseCase useCase;

    public EmployeeController(EmployeeUseCase useCase) {
        this.useCase = useCase;
    }

    @PostMapping
    public ResponseEntity<EmployeeResponseDTO> create(@Valid @RequestBody EmployeeRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(useCase.create(dto));
    }

    @GetMapping
    public ResponseEntity<List<EmployeeResponseDTO>> list() {
        return ResponseEntity.ok(useCase.list());
    }

    @GetMapping("/{employeeId}")
    public ResponseEntity<EmployeeResponseDTO> get(@PathVariable UUID employeeId) {
        return ResponseEntity.ok(useCase.getById(employeeId));
    }

    @PutMapping("/{employeeId}")
    public ResponseEntity<EmployeeResponseDTO> update(
            @PathVariable UUID employeeId,
            @Valid @RequestBody EmployeeUpdateDTO dto) {
        return ResponseEntity.ok(useCase.update(employeeId, dto));
    }

    @PatchMapping("/{employeeId}/status")
    public ResponseEntity<EmployeeResponseDTO> updateStatus(
            @PathVariable UUID employeeId,
            @Valid @RequestBody EmployeeStatusUpdateDTO dto) {
        return ResponseEntity.ok(useCase.updateStatus(employeeId, dto));
    }

    @DeleteMapping("/{employeeId}")
    public ResponseEntity<Void> delete(@PathVariable UUID employeeId) {
        useCase.delete(employeeId);
        return ResponseEntity.noContent().build();
    }
}
