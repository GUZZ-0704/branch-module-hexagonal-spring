package com.example.sucursal_api.image.adapter.out;

import com.example.sucursal_api.branch.adapter.out.BranchEntity;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "branch_image")
public class BranchImageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "branch_id", nullable = false)
    private BranchEntity branch;

    @Column(name = "url", nullable = false, length = 2048)
    private String url;

    @Column(name = "title", length = 120)
    private String title;

    @Column(name = "alt_text", length = 180)
    private String altText;

    @Column(name = "cover", nullable = false)
    private boolean cover = false;

    public BranchImageEntity() {}

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public BranchEntity getBranch() { return branch; }
    public void setBranch(BranchEntity branch) { this.branch = branch; }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAltText() { return altText; }
    public void setAltText(String altText) { this.altText = altText; }

    public boolean isCover() { return cover; }
    public void setCover(boolean cover) { this.cover = cover; }
}

