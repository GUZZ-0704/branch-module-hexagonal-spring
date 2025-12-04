package com.example.sucursal_api.assigment.application;

import com.example.sucursal_api.assigment.port.out.EmployeeBranchAssignmentRepository;
import com.example.sucursal_api.assigment.port.out.EmployeeCorporatePhoneAssignmentRepository;
import com.example.sucursal_api.assigment.domain.EmployeeBranchAssignment;
import com.example.sucursal_api.assigment.dto.AssignBranchRequestDTO;
import com.example.sucursal_api.assigment.dto.AssignmentResponseDTO;
import com.example.sucursal_api.assigment.dto.CloseAssignmentRequestDTO;
import com.example.sucursal_api.assigment.port.in.AssignBranchUseCase;
import com.example.sucursal_api.branch.port.out.BranchRepository;
import com.example.sucursal_api.employee.port.out.EmployeeRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class AssignBranchService implements AssignBranchUseCase {

    private final EmployeeBranchAssignmentRepository repo;
    private final EmployeeCorporatePhoneAssignmentRepository corpRepo;
    private final EmployeeRepository employeeRepo;
    private final BranchRepository branchRepo;

    public AssignBranchService(EmployeeBranchAssignmentRepository repo,
                               EmployeeCorporatePhoneAssignmentRepository corpRepo,
                               EmployeeRepository employeeRepo,
                               BranchRepository branchRepo) {
        this.repo = repo;
        this.corpRepo = corpRepo;
        this.employeeRepo = employeeRepo;
        this.branchRepo = branchRepo;
    }


    private void ensureEmployee(UUID employeeId) {
        try { employeeRepo.findById(employeeId); }
        catch (Exception e) { throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Empleado no encontrado: " + employeeId); }
    }

    private void ensureBranch(UUID branchId) {
        try { branchRepo.findById(branchId); }
        catch (Exception e) { throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Sucursal no encontrada: " + branchId); }
    }

    private LocalDate normalizeStart(LocalDate startDate) {
        return startDate != null ? startDate : LocalDate.now();
    }

    private LocalDate normalizeEnd(LocalDate endDate) {
        return endDate != null ? endDate : LocalDate.now();
    }


    @Override
    @Transactional
    public AssignmentResponseDTO assign(UUID employeeId, UUID branchId, AssignBranchRequestDTO dto) {
        ensureEmployee(employeeId);
        ensureBranch(branchId);

        var active = repo.findActiveByEmployee(employeeId);
        if (active != null) {
            active.setEndDate(normalizeEnd(null));
            repo.save(active);
            corpRepo.closeActiveByEmployee(employeeId);
        }

        var created = new EmployeeBranchAssignment(
                null,
                employeeId,
                branchId,
                normalizeStart(dto.startDate()),
                null,
                dto.position(),
                dto.notes()
        );

        var saved = repo.save(created);
        return new AssignmentResponseDTO(
                saved.getId(), saved.getEmployeeId(), saved.getBranchId(),
                saved.getStartDate(), saved.getEndDate(), saved.getPosition(),
                saved.getNotes(), saved.isActive()
        );
    }

    @Override
    @Transactional
    public AssignmentResponseDTO reassign(UUID employeeId, UUID toBranchId, AssignBranchRequestDTO dto) {
        return assign(employeeId, toBranchId, dto);
    }

    @Override
    @Transactional
    public AssignmentResponseDTO close(UUID assignmentId, CloseAssignmentRequestDTO dto) {
        var current = repo.findById(assignmentId);
        if (current.getEndDate() != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "La asignación ya está cerrada.");
        }
        current.setEndDate(normalizeEnd(dto.endDate()));
        var saved = repo.save(current);

        if (repo.findActiveByEmployee(saved.getEmployeeId()) == null) {
            corpRepo.closeActiveByEmployee(saved.getEmployeeId());
        }

        return new AssignmentResponseDTO(
                saved.getId(), saved.getEmployeeId(), saved.getBranchId(),
                saved.getStartDate(), saved.getEndDate(), saved.getPosition(),
                saved.getNotes(), saved.isActive()
        );
    }

    @Override
    @Transactional
    public AssignmentResponseDTO update(UUID assignmentId, AssignBranchRequestDTO dto) {
        var current = repo.findById(assignmentId);
        if (current.getEndDate() != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "La asignación ya está cerrada.");
        }

        if (dto.startDate() != null) {
            current.setStartDate(dto.startDate());
        }
        if (dto.position() != null) {
            current.setPosition(dto.position());
        }
        if (dto.notes() != null) {
            current.setNotes(dto.notes());
        }

        var saved = repo.save(current);
        return new AssignmentResponseDTO(
                saved.getId(), saved.getEmployeeId(), saved.getBranchId(),
                saved.getStartDate(), saved.getEndDate(), saved.getPosition(),
                saved.getNotes(), saved.isActive()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public AssignmentResponseDTO getActiveByEmployee(UUID employeeId) {
        ensureEmployee(employeeId);
        var a = repo.findActiveByEmployee(employeeId);
        if (a == null) return null;
        return new AssignmentResponseDTO(
                a.getId(), a.getEmployeeId(), a.getBranchId(),
                a.getStartDate(), a.getEndDate(), a.getPosition(), a.getNotes(), a.isActive()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<AssignmentResponseDTO> listActiveByBranch(UUID branchId) {
        ensureBranch(branchId);
        return repo.findActiveByBranch(branchId).stream()
                .map(a -> new AssignmentResponseDTO(
                        a.getId(), a.getEmployeeId(), a.getBranchId(),
                        a.getStartDate(), a.getEndDate(), a.getPosition(),
                        a.getNotes(), a.isActive()
                ))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AssignmentResponseDTO> listHistoryByEmployee(UUID employeeId) {
        ensureEmployee(employeeId);
        return repo.findHistoryByEmployee(employeeId).stream()
                .map(a -> new AssignmentResponseDTO(
                        a.getId(), a.getEmployeeId(), a.getBranchId(),
                        a.getStartDate(), a.getEndDate(), a.getPosition(),
                        a.getNotes(), a.isActive()
                ))
                .toList();
    }
}
