package com.example.sucursal_api.image.domain;


import java.util.UUID;

public class BranchImage {
    private UUID id;
    private UUID branchId;
    private String url;
    private String title;
    private String altText;
    private boolean cover;

    public BranchImage() {}

    public BranchImage(UUID id, UUID branchId, String url, String title,
                       String altText,  boolean cover) {
        this.id = id;
        this.branchId = branchId;
        this.url = url;
        this.title = title;
        this.altText = altText;
        this.cover = cover;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public UUID getBranchId() { return branchId; }
    public void setBranchId(UUID branchId) { this.branchId = branchId; }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAltText() { return altText; }
    public void setAltText(String altText) { this.altText = altText; }


    public boolean isCover() { return cover; }
    public void setCover(boolean cover) { this.cover = cover; }
}
