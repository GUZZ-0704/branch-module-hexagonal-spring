package com.example.sucursal_api.schedule.adapter.out;

import com.example.sucursal_api.branch.adapter.out.BranchEntity;
import jakarta.persistence.*;

import java.time.LocalTime;
import java.util.UUID;

@Entity
@Table(
        name = "branch_schedule",
        uniqueConstraints = @UniqueConstraint(name = "uq_branch_day", columnNames = {"branch_id", "day_of_week"})
)
public class ScheduleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "branch_id", nullable = false)
    private BranchEntity branch;

    @Column(name = "day_of_week", nullable = false)
    private int dayOfWeek;

    @Column(name = "open_time")
    private LocalTime open;

    @Column(name = "close_time")
    private LocalTime close;

    @Column(name = "closed", nullable = false)
    private boolean closed;

    public ScheduleEntity() {}

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public BranchEntity getBranch() { return branch; }
    public void setBranch(BranchEntity branch) { this.branch = branch; }

    public int getDayOfWeek() { return dayOfWeek; }
    public void setDayOfWeek(int dayOfWeek) { this.dayOfWeek = dayOfWeek; }

    public LocalTime getOpen() { return open; }
    public void setOpen(LocalTime open) { this.open = open; }

    public LocalTime getClose() { return close; }
    public void setClose(LocalTime close) { this.close = close; }

    public boolean isClosed() { return closed; }
    public void setClosed(boolean closed) { this.closed = closed; }
}
