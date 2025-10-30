package com.example.sucursal_api.employee.adapter.in;


import com.example.sucursal_api.employee.dto.EmployeePhoneRequestDTO;
import com.example.sucursal_api.employee.dto.EmployeePhoneResponseDTO;
import com.example.sucursal_api.employee.dto.EmployeePhoneUpdateDTO;
import com.example.sucursal_api.employee.port.in.EmployeePhoneUseCase;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/employees/{employeeId}/phones")
public class EmployeePhoneController {

    private final EmployeePhoneUseCase useCase;

    public EmployeePhoneController(EmployeePhoneUseCase useCase) {
        this.useCase = useCase;
    }

    @PostMapping
    public ResponseEntity<EmployeePhoneResponseDTO> add(
            @PathVariable UUID employeeId,
            @Valid @RequestBody EmployeePhoneRequestDTO dto) {
        return ResponseEntity.ok(useCase.add(employeeId, dto));
    }

    @GetMapping
    public ResponseEntity<List<EmployeePhoneResponseDTO>> list(@PathVariable UUID employeeId) {
        return ResponseEntity.ok(useCase.list(employeeId));
    }

    @PutMapping("/{phoneId}")
    public ResponseEntity<EmployeePhoneResponseDTO> update(
            @PathVariable UUID employeeId,
            @PathVariable UUID phoneId,
            @Valid @RequestBody EmployeePhoneUpdateDTO dto) {
        return ResponseEntity.ok(useCase.update(employeeId, phoneId, dto));
    }

    @DeleteMapping("/{phoneId}")
    public ResponseEntity<Void> delete(
            @PathVariable UUID employeeId,
            @PathVariable UUID phoneId) {
        useCase.delete(employeeId, phoneId);
        return ResponseEntity.noContent().build();
    }
}
