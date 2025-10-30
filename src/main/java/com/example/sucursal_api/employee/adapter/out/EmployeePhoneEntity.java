package com.example.sucursal_api.employee.adapter.out;


import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(
        name = "employee_phone",
        uniqueConstraints = @UniqueConstraint(name = "uq_employee_phone_number", columnNames = {"employee_id", "number"})
)
public class EmployeePhoneEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "employee_id", nullable = false)
    private EmployeeEntity employee;

    @Column(name = "number", nullable = false, length = 64)
    private String number;

    @Column(name = "label", length = 100)
    private String label;

    @Column(name = "whatsapp", nullable = false)
    private boolean whatsapp = false;

    @Column(name = "primary_phone", nullable = false)
    private boolean primary = false;

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public EmployeeEntity getEmployee() { return employee; }
    public void setEmployee(EmployeeEntity employee) { this.employee = employee; }

    public String getNumber() { return number; }
    public void setNumber(String number) { this.number = number; }

    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }

    public boolean isWhatsapp() { return whatsapp; }
    public void setWhatsapp(boolean whatsapp) { this.whatsapp = whatsapp; }

    public boolean isPrimary() { return primary; }
    public void setPrimary(boolean primary) { this.primary = primary; }
}
