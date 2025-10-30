package com.example.sucursal_api.assigment.domain;


import java.time.LocalDate;
import java.util.UUID;

public class EmployeeBranchAssignment {
    private UUID id;
    private UUID employeeId;
    private UUID branchId;
    private LocalDate startDate;
    private LocalDate endDate;
    private String position;
    private String notes;

    public EmployeeBranchAssignment() {}

    public EmployeeBranchAssignment(UUID id, UUID employeeId, UUID branchId,
                                    LocalDate startDate, LocalDate endDate,
                                    String position, String notes) {
        this.id = id;
        this.employeeId = employeeId;
        this.branchId = branchId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.position = position;
        this.notes = notes;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public UUID getEmployeeId() { return employeeId; }
    public void setEmployeeId(UUID employeeId) { this.employeeId = employeeId; }

    public UUID getBranchId() { return branchId; }
    public void setBranchId(UUID branchId) { this.branchId = branchId; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public boolean isActive() { return endDate == null || endDate.isAfter(LocalDate.now()); }
}
