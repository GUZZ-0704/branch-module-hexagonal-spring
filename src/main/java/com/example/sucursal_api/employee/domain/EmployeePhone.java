package com.example.sucursal_api.employee.domain;

import java.util.UUID;

public class EmployeePhone {
    private UUID id;
    private UUID employeeId;
    private String number;
    private String label;
    private boolean whatsapp;
    private boolean primary;

    public EmployeePhone() {}

    public EmployeePhone(UUID id, UUID employeeId, String number, String label,
                         boolean whatsapp, boolean primary) {
        this.id = id;
        this.employeeId = employeeId;
        this.number = number;
        this.label = label;
        this.whatsapp = whatsapp;
        this.primary = primary;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public UUID getEmployeeId() { return employeeId; }
    public void setEmployeeId(UUID employeeId) { this.employeeId = employeeId; }

    public String getNumber() { return number; }
    public void setNumber(String number) { this.number = number; }

    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }

    public boolean isWhatsapp() { return whatsapp; }
    public void setWhatsapp(boolean whatsapp) { this.whatsapp = whatsapp; }

    public boolean isPrimary() { return primary; }
    public void setPrimary(boolean primary) { this.primary = primary; }
}
