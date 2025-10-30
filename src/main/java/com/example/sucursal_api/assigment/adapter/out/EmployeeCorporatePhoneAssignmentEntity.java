package com.example.sucursal_api.assigment.adapter.out;

import com.example.sucursal_api.branch.adapter.out.BranchEntity;
import com.example.sucursal_api.employee.adapter.out.EmployeeEntity;
import com.example.sucursal_api.phone.adapter.out.BranchPhoneEntity;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(
        name = "employee_corp_phone_assignment",
        indexes = {
                @Index(name = "ix_ecpa_employee_active", columnList = "employee_id,end_date"),
                @Index(name = "ix_ecpa_phone_active", columnList = "branch_phone_id,end_date")
        }
)
public class EmployeeCorporatePhoneAssignmentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "employee_id", nullable = false)
    private EmployeeEntity employee;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "branch_id", nullable = false)
    private BranchEntity branch;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "branch_phone_id", nullable = false)
    private BranchPhoneEntity branchPhone;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public EmployeeEntity getEmployee() { return employee; }
    public void setEmployee(EmployeeEntity employee) { this.employee = employee; }
    public BranchEntity getBranch() { return branch; }
    public void setBranch(BranchEntity branch) { this.branch = branch; }
    public BranchPhoneEntity getBranchPhone() { return branchPhone; }
    public void setBranchPhone(BranchPhoneEntity branchPhone) { this.branchPhone = branchPhone; }
    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
}
