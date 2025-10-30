package com.example.sucursal_api.branch.domain;

import java.math.BigDecimal;
import java.util.UUID;

public class Branch {
    private UUID id;
    private String name;
    private String slug;
    private String address;
    private String primaryPhone;
    private BigDecimal lat;
    private BigDecimal lng;
    private boolean active;

    public Branch() {}

    public Branch(UUID id, String name, String slug, String address,
                  String primaryPhone, BigDecimal lat, BigDecimal lng, boolean active) {
        this.id = id;
        this.name = name;
        this.slug = slug;
        this.address = address;
        this.primaryPhone = primaryPhone;
        this.lat = lat;
        this.lng = lng;
        this.active = active;
    }

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