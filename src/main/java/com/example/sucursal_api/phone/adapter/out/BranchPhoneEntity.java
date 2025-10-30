package com.example.sucursal_api.phone.adapter.out;


import com.example.sucursal_api.branch.adapter.out.BranchEntity;
import com.example.sucursal_api.phone.domain.PhoneKind;
import com.example.sucursal_api.phone.domain.PhoneState;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(
        name = "branch_phone",
        uniqueConstraints = @UniqueConstraint(name = "uq_branch_phone_number", columnNames = {"branch_id", "number"})
)
public class BranchPhoneEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "branch_id", nullable = false)
    private BranchEntity branch;

    @Column(name = "number", nullable = false, length = 64)
    private String number;

    @Enumerated(EnumType.STRING)
    @Column(name = "kind", nullable = false, length = 16)
    private PhoneKind kind;

    @Enumerated(EnumType.STRING)
    @Column(name = "state", length = 16)
    private PhoneState state;

    @Column(name = "label", length = 100)
    private String label;

    @Column(name = "whatsapp", nullable = false)
    private boolean whatsapp = false;

    @Column(name = "publish", nullable = false)
    private boolean publish = false;

    @Column(name = "priority", nullable = false)
    private int priority = 0;

    public BranchPhoneEntity() {}

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public BranchEntity getBranch() { return branch; }
    public void setBranch(BranchEntity branch) { this.branch = branch; }

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
