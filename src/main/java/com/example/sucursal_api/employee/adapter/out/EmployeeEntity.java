package com.example.sucursal_api.employee.adapter.out;


import com.example.sucursal_api.employee.domain.EmployeeStatus;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(
        name = "employee",
        indexes = {
                @Index(name = "ix_employee_doc_number", columnList = "doc_number"),
                @Index(name = "ix_employee_status", columnList = "status")
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_employee_institutional_email", columnNames = "institutional_email")
        }
)
public class EmployeeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "first_name", nullable = false, length = 120)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 160)
    private String lastName;

    @Column(name = "doc_type", length = 20)
    private String docType;

    @Column(name = "doc_number", length = 50)
    private String docNumber;

    @Column(name = "personal_email", length = 255)
    private String personalEmail;

    @Column(name = "institutional_email", nullable = false, length = 255)
    private String institutionalEmail;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 16)
    private EmployeeStatus status = EmployeeStatus.ACTIVE;

    @Column(name = "hire_date")
    private LocalDate hireDate;

    @Column(name = "termination_date")
    private LocalDate terminationDate;

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getDocType() { return docType; }
    public void setDocType(String docType) { this.docType = docType; }
    public String getDocNumber() { return docNumber; }
    public void setDocNumber(String docNumber) { this.docNumber = docNumber; }
    public String getPersonalEmail() { return personalEmail; }
    public void setPersonalEmail(String personalEmail) { this.personalEmail = personalEmail; }
    public String getInstitutionalEmail() { return institutionalEmail; }
    public void setInstitutionalEmail(String institutionalEmail) { this.institutionalEmail = institutionalEmail; }
    public EmployeeStatus getStatus() { return status; }
    public void setStatus(EmployeeStatus status) { this.status = status; }
    public LocalDate getHireDate() { return hireDate; }
    public void setHireDate(LocalDate hireDate) { this.hireDate = hireDate; }
    public LocalDate getTerminationDate() { return terminationDate; }
    public void setTerminationDate(LocalDate terminationDate) { this.terminationDate = terminationDate; }
}
