package com.example.sucursal_api.employee.port.in;

import com.example.sucursal_api.employee.dto.EmployeeRequestDTO;
import com.example.sucursal_api.employee.dto.EmployeeResponseDTO;
import com.example.sucursal_api.employee.dto.EmployeeStatusUpdateDTO;
import com.example.sucursal_api.employee.dto.EmployeeUpdateDTO;

import java.util.List;
import java.util.UUID;

public interface EmployeeUseCase {
    EmployeeResponseDTO create(EmployeeRequestDTO dto);
    EmployeeResponseDTO getById(UUID id);
    List<EmployeeResponseDTO> list();
    EmployeeResponseDTO update(UUID id, EmployeeUpdateDTO dto);
    EmployeeResponseDTO updateStatus(UUID id, EmployeeStatusUpdateDTO dto);
    void delete(UUID id);
}
