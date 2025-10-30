package com.example.sucursal_api.employee.application;

import com.example.sucursal_api.employee.application.mapper.EmployeePhoneMapper;
import com.example.sucursal_api.employee.domain.EmployeePhone;
import com.example.sucursal_api.employee.dto.EmployeePhoneRequestDTO;
import com.example.sucursal_api.employee.dto.EmployeePhoneResponseDTO;
import com.example.sucursal_api.employee.dto.EmployeePhoneUpdateDTO;
import com.example.sucursal_api.employee.port.in.EmployeePhoneUseCase;
import com.example.sucursal_api.employee.port.out.EmployeePhoneRepository;
import com.example.sucursal_api.employee.port.out.EmployeeRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
public class EmployeePhoneService implements EmployeePhoneUseCase {

    private final EmployeePhoneRepository phoneRepo;
    private final EmployeeRepository employeeRepo;
    private final EmployeePhoneMapper mapper;

    public EmployeePhoneService(EmployeePhoneRepository phoneRepo, EmployeeRepository employeeRepo, EmployeePhoneMapper mapper) {
        this.phoneRepo = phoneRepo;
        this.employeeRepo = employeeRepo;
        this.mapper = mapper;
    }

    private void ensureEmployeeExists(UUID employeeId) {
        try { employeeRepo.findById(employeeId); }
        catch (Exception e) { throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Empleado no encontrado: " + employeeId); }
    }

    private String normalizeNumber(String raw) {
        return raw == null ? null : raw.trim().replaceAll("\\s+", " ");
    }

    private void ensureBelongs(EmployeePhone phone, UUID employeeId) {
        if (!phone.getEmployeeId().equals(employeeId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El teléfono no pertenece al empleado especificado.");
        }
    }

    private void ensureSinglePrimary(UUID employeeId, boolean makePrimary, UUID excludeId) {
        if (!makePrimary) return;
        List<EmployeePhone> all = phoneRepo.findByEmployee(employeeId);
        for (EmployeePhone p : all) {
            if (excludeId != null && p.getId().equals(excludeId)) continue;
            if (p.isPrimary()) {
                p.setPrimary(false);
                phoneRepo.save(p);
            }
        }
    }

    @Override
    @Transactional
    public EmployeePhoneResponseDTO add(UUID employeeId, EmployeePhoneRequestDTO dto) {
        ensureEmployeeExists(employeeId);

        String number = normalizeNumber(dto.number());
        if (phoneRepo.existsByEmployeeAndNumber(employeeId, number)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El empleado ya tiene registrado ese número.");
        }

        boolean primary = dto.primary() != null && dto.primary();
        ensureSinglePrimary(employeeId, primary, null);

        EmployeePhone phone = new EmployeePhone(
                null, employeeId, number, dto.label(),
                dto.whatsapp() != null && dto.whatsapp(), primary
        );

        return mapper.toResponseDTO(phoneRepo.save(phone));
    }

    @Override
    @Transactional
    public EmployeePhoneResponseDTO update(UUID employeeId, UUID phoneId, EmployeePhoneUpdateDTO dto) {
        ensureEmployeeExists(employeeId);

        EmployeePhone current = phoneRepo.findById(phoneId);
        ensureBelongs(current, employeeId);

        if (dto.number() != null) {
            String newNumber = normalizeNumber(dto.number());
            if (!newNumber.equals(current.getNumber())
                    && phoneRepo.existsByEmployeeAndNumber(employeeId, newNumber)) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "El empleado ya tiene registrado ese número.");
            }
            current.setNumber(newNumber);
        }
        if (dto.label() != null) current.setLabel(dto.label());
        if (dto.whatsapp() != null) current.setWhatsapp(dto.whatsapp());
        if (dto.primary() != null) {
            boolean makePrimary = dto.primary();
            ensureSinglePrimary(employeeId, makePrimary, current.getId());
            current.setPrimary(makePrimary);
        }

        return mapper.toResponseDTO(phoneRepo.save(current));
    }

    @Override
    @Transactional
    public void delete(UUID employeeId, UUID phoneId) {
        ensureEmployeeExists(employeeId);
        EmployeePhone current = phoneRepo.findById(phoneId);
        ensureBelongs(current, employeeId);
        phoneRepo.delete(phoneId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeePhoneResponseDTO> list(UUID employeeId) {
        ensureEmployeeExists(employeeId);
        return phoneRepo.findByEmployee(employeeId).stream()
                .map(mapper::toResponseDTO)
                .toList();
    }
}
