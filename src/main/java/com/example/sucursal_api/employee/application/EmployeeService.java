package com.example.sucursal_api.employee.application;


import com.example.sucursal_api.employee.application.mapper.EmployeeMapper;
import com.example.sucursal_api.employee.domain.Employee;
import com.example.sucursal_api.employee.domain.EmployeeStatus;
import com.example.sucursal_api.employee.dto.EmployeeRequestDTO;
import com.example.sucursal_api.employee.dto.EmployeeResponseDTO;
import com.example.sucursal_api.employee.dto.EmployeeStatusUpdateDTO;
import com.example.sucursal_api.employee.dto.EmployeeUpdateDTO;
import com.example.sucursal_api.employee.port.in.EmployeeUseCase;
import com.example.sucursal_api.employee.port.out.EmployeeRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.text.Normalizer;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;



@Service
public class EmployeeService implements EmployeeUseCase {

    private final EmployeeRepository repo;
    private final EmployeeMapper mapper;

    private final String institutionalDomain = "empresa.com";

    public EmployeeService(EmployeeRepository repo, EmployeeMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    private String slugify(String text) {
        if (text == null) return "";
        String nfd = Normalizer.normalize(text, Normalizer.Form.NFD);
        String noAccents = nfd.replaceAll("\\p{M}+", "");
        String lower = noAccents.toLowerCase();
        String safe = lower.replaceAll("[^a-z0-9]+", ".");
        String compact = safe.replaceAll("\\.{2,}", ".");
        return compact.replaceAll("^\\.|\\.$", "");
    }

    private String generateInstitutionalEmail(String firstName, String lastName) {
        String base = (slugify(firstName) + "." + slugify(lastName)).replaceAll("\\.+", ".");
        if (base.isBlank()) base = "user";
        String candidate = base + "@" + institutionalDomain;
        if (!repo.existsByInstitutionalEmail(candidate)) return candidate;

        int i = 2;
        while (true) {
            String next = base + "-" + i + "@" + institutionalDomain;
            if (!repo.existsByInstitutionalEmail(next)) return next;
            i++;
            if (i > 9999) throw new ResponseStatusException(HttpStatus.CONFLICT, "No fue posible generar un correo institucional único");
        }
    }

    @Override
    @Transactional
    public EmployeeResponseDTO create(EmployeeRequestDTO dto) {
        Employee emp = new Employee(
                null,
                dto.firstName(),
                dto.lastName(),
                dto.docType(),
                dto.docNumber(),
                dto.personalEmail(),
                generateInstitutionalEmail(dto.firstName(), dto.lastName()),
                EmployeeStatus.ACTIVE,
                dto.hireDate() != null ? dto.hireDate() : LocalDate.now(),
                null
        );
        return mapper.toResponseDTO(repo.save(emp));
    }

    @Override
    @Transactional(readOnly = true)
    public EmployeeResponseDTO getById(UUID id) {
        return mapper.toResponseDTO(repo.findById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeResponseDTO> list() {
        return repo.findAll().stream().map(mapper::toResponseDTO).toList();
    }

    @Override
    @Transactional
    public EmployeeResponseDTO update(UUID id, EmployeeUpdateDTO dto) {
        Employee current = repo.findById(id);
        if (dto.firstName() != null) current.setFirstName(dto.firstName());
        if (dto.lastName() != null) current.setLastName(dto.lastName());
        if (dto.docType() != null) current.setDocType(dto.docType());
        if (dto.docNumber() != null) current.setDocNumber(dto.docNumber());
        if (dto.personalEmail() != null) current.setPersonalEmail(dto.personalEmail());
        if (dto.hireDate() != null) current.setHireDate(dto.hireDate());
        if (dto.terminationDate() != null) current.setTerminationDate(dto.terminationDate());
        return mapper.toResponseDTO(repo.save(current));
    }

    @Override
    @Transactional
    public EmployeeResponseDTO updateStatus(UUID id, EmployeeStatusUpdateDTO dto) {
        Employee current = repo.findById(id);
        current.setStatus(dto.status());
        if (dto.status() == EmployeeStatus.INACTIVE && current.getTerminationDate() == null) {
            current.setTerminationDate(LocalDate.now());
        }
        return mapper.toResponseDTO(repo.save(current));
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        repo.delete(id);
    }
}
