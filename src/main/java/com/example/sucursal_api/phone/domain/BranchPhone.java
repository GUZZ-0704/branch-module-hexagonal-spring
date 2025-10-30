package com.example.sucursal_api.phone.domain;


import java.util.UUID;

public class BranchPhone {
    private UUID id;
    private UUID branchId;
    private String number;
    private PhoneKind kind;
    private PhoneState state;
    private String label;
    private boolean whatsapp;
    private boolean publish;
    private int priority;

    public BranchPhone() {}

    public BranchPhone(UUID id, UUID branchId, String number, PhoneKind kind, PhoneState state,
                       String label, boolean whatsapp, boolean publish, int priority) {
        this.id = id;
        this.branchId = branchId;
        this.number = number;
        this.kind = kind;
        this.state = state;
        this.label = label;
        this.whatsapp = whatsapp;
        this.publish = publish;
        this.priority = priority;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public UUID getBranchId() { return branchId; }
    public void setBranchId(UUID branchId) { this.branchId = branchId; }

    public String getNumber() { return number; }
    public void setNumber(String number) { this.number = number; }

    public PhoneKind getKind() { return kind; }
    public void setKind(PhoneKind kind) { this.kind = kind; }

    public PhoneState getState() { return state; }
    public void setState(PhoneState state) { this.state = state; }

    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }

    public boolean isWhatsapp() { return whatsapp; }
    public void setWhatsapp(boolean whatsapp) { this.whatsapp = whatsapp; }

    public boolean isPublish() { return publish; }
    public void setPublish(boolean publish) { this.publish = publish; }

    public int getPriority() { return priority; }
    public void setPriority(int priority) { this.priority = priority; }
}
