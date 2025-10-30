package com.example.sucursal_api.employee.port.in;


import com.example.sucursal_api.employee.dto.EmployeePhoneRequestDTO;
import com.example.sucursal_api.employee.dto.EmployeePhoneResponseDTO;
import com.example.sucursal_api.employee.dto.EmployeePhoneUpdateDTO;

import java.util.List;
import java.util.UUID;

public interface EmployeePhoneUseCase {
    EmployeePhoneResponseDTO add(UUID employeeId, EmployeePhoneRequestDTO dto);
    EmployeePhoneResponseDTO update(UUID employeeId, UUID phoneId, EmployeePhoneUpdateDTO dto);
    void delete(UUID employeeId, UUID phoneId);
    List<EmployeePhoneResponseDTO> list(UUID employeeId);
}
