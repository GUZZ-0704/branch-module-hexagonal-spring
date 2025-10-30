package com.example.sucursal_api.assigment.domain;


import java.time.LocalDate;
import java.util.UUID;


public class EmployeeCorporatePhoneAssignment {
    private UUID id;
    private UUID employeeId;
    private UUID branchId;
    private UUID branchPhoneId;
    private LocalDate startDate;
    private LocalDate endDate;

    public EmployeeCorporatePhoneAssignment() {}

    public EmployeeCorporatePhoneAssignment(UUID id, UUID employeeId, UUID branchId, UUID branchPhoneId,
                                            LocalDate startDate, LocalDate endDate) {
        this.id = id;
        this.employeeId = employeeId;
        this.branchId = branchId;
        this.branchPhoneId = branchPhoneId;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public UUID getEmployeeId() { return employeeId; }
    public void setEmployeeId(UUID employeeId) { this.employeeId = employeeId; }

    public UUID getBranchId() { return branchId; }
    public void setBranchId(UUID branchId) { this.branchId = branchId; }

    public UUID getBranchPhoneId() { return branchPhoneId; }
    public void setBranchPhoneId(UUID branchPhoneId) { this.branchPhoneId = branchPhoneId; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public boolean isActive() { return endDate == null || endDate.isAfter(LocalDate.now()); }
}

