package com.example.sucursal_api.common;

import com.example.sucursal_api.assigment.port.out.EmployeeBranchAssignmentRepository;
import com.example.sucursal_api.employee.port.out.EmployeeRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.Set;
import java.util.UUID;

@Component
public class OwnershipValidator {

    private final UserProvider userProvider;
    private final EmployeeRepository employeeRepo;
    private final EmployeeBranchAssignmentRepository assignmentRepo;

    public OwnershipValidator(UserProvider userProvider,
                              EmployeeRepository employeeRepo,
                              EmployeeBranchAssignmentRepository assignmentRepo) {
        this.userProvider = userProvider;
        this.employeeRepo = employeeRepo;
        this.assignmentRepo = assignmentRepo;
    }


    private User requireAuthenticated() {
        User u = userProvider.getCurrentUser();
        if (u == null) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No autenticado");
        return u;
    }

    private boolean isAdmin(Set<String> roles) {
        return roles != null && roles.stream().anyMatch(r -> r.equalsIgnoreCase("ADMIN"));
    }


    public void requireAdmin() {
        User u = requireAuthenticated();
        if (!isAdmin(u.getRoles())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Solo ADMIN puede realizar esta operación");
        }
    }

    public void requireAdminOrSelfEmployee(UUID employeeId) {
        User u = requireAuthenticated();
        if (isAdmin(u.getRoles())) return;
        if (u.getEmployeeId() == null || !u.getEmployeeId().equals(employeeId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No puedes operar sobre otro empleado");
        }
    }


    public void requireAdminOrAssignedToBranch(UUID branchId) {
        User u = requireAuthenticated();
        if (isAdmin(u.getRoles())) return;

        if (u.getEmployeeId() == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Empleado no asociado");
        }

        var active = assignmentRepo.findActiveByEmployee(u.getEmployeeId());
        if (active == null || !active.getBranchId().equals(branchId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No estás asignado a esta sucursal");
        }
    }


    public void validateEmployeeAssignedToBranch(UUID employeeId, UUID branchId) {
        try { employeeRepo.findById(employeeId); }
        catch (Exception e) { throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Empleado no existe"); }

        var active = assignmentRepo.findActiveByEmployee(employeeId);
        if (active == null || !active.getBranchId().equals(branchId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El empleado no está asignado a la sucursal indicada");
        }
    }
}
