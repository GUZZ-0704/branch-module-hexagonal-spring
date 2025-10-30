package com.example.sucursal_api.schedule.adapter.in;

import com.example.sucursal_api.schedule.dto.ScheduleRequestDTO;
import com.example.sucursal_api.schedule.dto.ScheduleResponseDTO;
import com.example.sucursal_api.schedule.dto.ScheduleUpdateDTO;
import com.example.sucursal_api.schedule.port.in.ScheduleUseCase;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/branches/{branchId}/schedules")
public class ScheduleController {

    private final ScheduleUseCase useCase;

    public ScheduleController(ScheduleUseCase useCase) {
        this.useCase = useCase;
    }

    @PutMapping
    public ResponseEntity<ScheduleResponseDTO> upsert(
            @PathVariable UUID branchId,
            @Valid @RequestBody ScheduleRequestDTO dto) {
        return ResponseEntity.ok(useCase.upsert(branchId, dto));
    }

    @PatchMapping("/{dayOfWeek}")
    public ResponseEntity<ScheduleResponseDTO> update(
            @PathVariable UUID branchId,
            @PathVariable int dayOfWeek,
            @Valid @RequestBody ScheduleUpdateDTO dto) {
        return ResponseEntity.ok(useCase.update(branchId, dayOfWeek, dto));
    }

    @GetMapping
    public ResponseEntity<List<ScheduleResponseDTO>> getWeek(@PathVariable UUID branchId) {
        return ResponseEntity.ok(useCase.getWeek(branchId));
    }

    @GetMapping("/{dayOfWeek}")
    public ResponseEntity<ScheduleResponseDTO> getDay(
            @PathVariable UUID branchId,
            @PathVariable int dayOfWeek) {
        return ResponseEntity.ok(useCase.getDay(branchId, dayOfWeek));
    }

    @DeleteMapping("/{dayOfWeek}")
    public ResponseEntity<Void> deleteDay(
            @PathVariable UUID branchId,
            @PathVariable int dayOfWeek) {
        useCase.deleteDay(branchId, dayOfWeek);
        return ResponseEntity.noContent().build();
    }
}
