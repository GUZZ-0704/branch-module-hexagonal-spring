package com.example.sucursal_api.assigment.adapter.out;

import com.example.sucursal_api.branch.adapter.out.BranchEntity;
import com.example.sucursal_api.employee.adapter.out.EmployeeEntity;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(
        name = "employee_branch_assignment",
        indexes = {
                @Index(name = "ix_eba_employee_active", columnList = "employee_id,end_date"),
                @Index(name = "ix_eba_branch_active", columnList = "branch_id,end_date")
        }
)
public class EmployeeBranchAssignmentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "employee_id", nullable = false)
    private EmployeeEntity employee;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "branch_id", nullable = false)
    private BranchEntity branch;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate; // null = activa

    @Column(name = "position", length = 80)
    private String position;

    @Column(name = "notes", length = 200)
    private String notes;

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public EmployeeEntity getEmployee() { return employee; }
    public void setEmployee(EmployeeEntity employee) { this.employee = employee; }
    public BranchEntity getBranch() { return branch; }
    public void setBranch(BranchEntity branch) { this.branch = branch; }
    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}

