package com.example.sucursal_api.assigment.application;

import com.example.sucursal_api.assigment.domain.EmployeeCorporatePhoneAssignment;
import com.example.sucursal_api.assigment.dto.AssignCorpPhoneRequestDTO;
import com.example.sucursal_api.assigment.dto.CloseAssignmentRequestDTO;
import com.example.sucursal_api.assigment.dto.CorpPhoneAssignmentResponseDTO;
import com.example.sucursal_api.assigment.port.in.AssignCorpPhoneUseCase;
import com.example.sucursal_api.assigment.port.out.EmployeeBranchAssignmentRepository;
import com.example.sucursal_api.assigment.port.out.EmployeeCorporatePhoneAssignmentRepository;
import com.example.sucursal_api.branch.port.out.BranchRepository;
import com.example.sucursal_api.employee.port.out.EmployeeRepository;
import com.example.sucursal_api.phone.domain.BranchPhone;
import com.example.sucursal_api.phone.domain.PhoneKind;
import com.example.sucursal_api.phone.domain.PhoneState;
import com.example.sucursal_api.phone.port.out.BranchPhoneRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.UUID;

@Service
public class AssignCorpPhoneService implements AssignCorpPhoneUseCase {

    private final EmployeeCorporatePhoneAssignmentRepository repo;
    private final EmployeeRepository employeeRepo;
    private final BranchRepository branchRepo;
    private final BranchPhoneRepository phoneRepo;
    private final EmployeeBranchAssignmentRepository branchAssignRepo;

    public AssignCorpPhoneService(EmployeeCorporatePhoneAssignmentRepository repo,
                                  EmployeeRepository employeeRepo,
                                  BranchRepository branchRepo,
                                  BranchPhoneRepository phoneRepo,
                                  EmployeeBranchAssignmentRepository branchAssignRepo) {
        this.repo = repo;
        this.employeeRepo = employeeRepo;
        this.branchRepo = branchRepo;
        this.phoneRepo = phoneRepo;
        this.branchAssignRepo = branchAssignRepo;
    }


    private void ensureEmployee(UUID employeeId) {
        try { employeeRepo.findById(employeeId); }
        catch (Exception e) { throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Empleado no encontrado: " + employeeId); }
    }

    private BranchPhone ensureCorporatePhone(UUID branchPhoneId) {
        BranchPhone p = phoneRepo.findById(branchPhoneId);
        if (p.getKind() != PhoneKind.CORPORATE) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El teléfono no es CORPORATE.");
        }
        return p;
    }

    private void ensureEmployeeActiveBranchMatchesPhone(UUID employeeId, BranchPhone phone) {
        var activeAssign = branchAssignRepo.findActiveByEmployee(employeeId);
        if (activeAssign == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El empleado no tiene asignación activa de sucursal.");
        }
        if (!activeAssign.getBranchId().equals(phone.getBranchId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "El teléfono corporativo pertenece a otra sucursal. No se puede asignar.");
        }
        try { branchRepo.findById(phone.getBranchId()); } catch (Exception ignore) {}
    }

    private LocalDate normalizeStart(LocalDate startDate) {
        return startDate != null ? startDate : LocalDate.now();
    }


    @Override
    @Transactional
    public CorpPhoneAssignmentResponseDTO assign(UUID employeeId, UUID branchPhoneId, AssignCorpPhoneRequestDTO dto) {
        ensureEmployee(employeeId);
        BranchPhone phone = ensureCorporatePhone(branchPhoneId);
        ensureEmployeeActiveBranchMatchesPhone(employeeId, phone);

        var activeByEmp = repo.findActiveByEmployee(employeeId);
        if (activeByEmp != null) {
            activeByEmp.setEndDate(LocalDate.now());
            repo.save(activeByEmp);
            try {
                BranchPhone prevPhone = phoneRepo.findById(activeByEmp.getBranchPhoneId());
                prevPhone.setState(PhoneState.AVAILABLE);
                phoneRepo.save(prevPhone);
            } catch (Exception ignore) {}
        }

        var activeByPhone = repo.findActiveByPhone(branchPhoneId);
        if (activeByPhone != null) {
            activeByPhone.setEndDate(LocalDate.now());
            repo.save(activeByPhone);
        }

        phone.setState(PhoneState.ASSIGNED);
        phoneRepo.save(phone);

        var created = new EmployeeCorporatePhoneAssignment(
                null,
                employeeId,
                phone.getBranchId(),
                phone.getId(),
                normalizeStart(dto.startDate()),
                null
        );
        var saved = repo.save(created);

        return new CorpPhoneAssignmentResponseDTO(
                saved.getId(), saved.getEmployeeId(), saved.getBranchId(),
                saved.getBranchPhoneId(), saved.getStartDate(), saved.getEndDate(), saved.isActive()
        );
    }

    @Override
    @Transactional
    public CorpPhoneAssignmentResponseDTO close(UUID corpPhoneAssignmentId, CloseAssignmentRequestDTO dto) {
        var current = repo.findById(corpPhoneAssignmentId);
        if (current.getEndDate() != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "La asignación ya está cerrada.");
        }
        current.setEndDate(dto.endDate() != null ? dto.endDate() : LocalDate.now());
        var saved = repo.save(current);

        BranchPhone phone = phoneRepo.findById(saved.getBranchPhoneId());
        phone.setState(PhoneState.AVAILABLE);
        phoneRepo.save(phone);

        return new CorpPhoneAssignmentResponseDTO(
                saved.getId(), saved.getEmployeeId(), saved.getBranchId(),
                saved.getBranchPhoneId(), saved.getStartDate(), saved.getEndDate(), saved.isActive()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public CorpPhoneAssignmentResponseDTO getActiveByEmployee(UUID employeeId) {
        ensureEmployee(employeeId);
        var a = repo.findActiveByEmployee(employeeId);
        if (a == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El empleado no tiene corporativo asignado.");
        return new CorpPhoneAssignmentResponseDTO(
                a.getId(), a.getEmployeeId(), a.getBranchId(),
                a.getBranchPhoneId(), a.getStartDate(), a.getEndDate(), a.isActive()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public CorpPhoneAssignmentResponseDTO getActiveByPhone(UUID branchPhoneId) {
        ensureCorporatePhone(branchPhoneId);
        var a = repo.findActiveByPhone(branchPhoneId);
        if (a == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El teléfono no está asignado actualmente.");
        return new CorpPhoneAssignmentResponseDTO(
                a.getId(), a.getEmployeeId(), a.getBranchId(),
                a.getBranchPhoneId(), a.getStartDate(), a.getEndDate(), a.isActive()
        );
    }
}

