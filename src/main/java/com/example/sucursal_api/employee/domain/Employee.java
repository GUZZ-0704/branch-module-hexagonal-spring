package com.example.sucursal_api.employee.domain;

import java.time.LocalDate;
import java.util.UUID;

public class Employee {
    private UUID id;
    private String firstName;
    private String lastName;
    private String docType;
    private String docNumber;
    private String personalEmail;
    private String institutionalEmail;
    private EmployeeStatus status;
    private LocalDate hireDate;
    private LocalDate terminationDate;

    public Employee() {}

    public Employee(UUID id, String firstName, String lastName, String docType, String docNumber,
                    String personalEmail, String institutionalEmail, EmployeeStatus status,
                    LocalDate hireDate, LocalDate terminationDate) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.docType = docType;
        this.docNumber = docNumber;
        this.personalEmail = personalEmail;
        this.institutionalEmail = institutionalEmail;
        this.status = status;
        this.hireDate = hireDate;
        this.terminationDate = terminationDate;
    }

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
