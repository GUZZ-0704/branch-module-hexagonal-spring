package com.example.sucursal_api.branch.adapter.out;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "branch")
public class BranchEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true, length = 64)
    private String slug;

    @Column(nullable = false)
    private String address;

    private String primaryPhone;

    @Column(nullable = false, precision = 9, scale = 6)
    private BigDecimal lat;

    @Column(nullable = false, precision = 9, scale = 6)
    private BigDecimal lng;

    @Column(nullable = false)
    private boolean active = true;

    public BranchEntity() {}

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSlug() { return slug; }
    public void setSlug(String slug) { this.slug = slug; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getPrimaryPhone() { return primaryPhone; }
    public void setPrimaryPhone(String primaryPhone) { this.primaryPhone = primaryPhone; }

    public BigDecimal getLat() { return lat; }
    public void setLat(BigDecimal lat) { this.lat = lat; }

    public BigDecimal getLng() { return lng; }
    public void setLng(BigDecimal lng) { this.lng = lng; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}
