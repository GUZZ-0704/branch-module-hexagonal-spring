package com.example.sucursal_api.schedule.domain;

import java.time.LocalTime;
import java.util.UUID;

public class Schedule {
    private UUID id;
    private UUID branchId;
    private int dayOfWeek;
    private LocalTime open;
    private LocalTime close;
    private boolean closed;

    public Schedule() {}

    public Schedule(UUID id, UUID branchId, int dayOfWeek, LocalTime open, LocalTime close, boolean closed) {
        this.id = id;
        this.branchId = branchId;
        this.dayOfWeek = dayOfWeek;
        this.open = open;
        this.close = close;
        this.closed = closed;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public UUID getBranchId() { return branchId; }
    public void setBranchId(UUID branchId) { this.branchId = branchId; }

    public int getDayOfWeek() { return dayOfWeek; }
    public void setDayOfWeek(int dayOfWeek) { this.dayOfWeek = dayOfWeek; }

    public LocalTime getOpen() { return open; }
    public void setOpen(LocalTime open) { this.open = open; }

    public LocalTime getClose() { return close; }
    public void setClose(LocalTime close) { this.close = close; }

    public boolean isClosed() { return closed; }
    public void setClosed(boolean closed) { this.closed = closed; }
}
