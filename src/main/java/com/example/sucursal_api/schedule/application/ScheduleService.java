package com.example.sucursal_api.schedule.application;

import com.example.sucursal_api.branch.port.out.BranchRepository;
import com.example.sucursal_api.schedule.application.mapper.ScheduleMapper;
import com.example.sucursal_api.schedule.domain.Schedule;
import com.example.sucursal_api.schedule.dto.*;
import com.example.sucursal_api.schedule.port.in.ScheduleUseCase;
import com.example.sucursal_api.schedule.port.out.ScheduleRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
public class ScheduleService implements ScheduleUseCase {

    private final ScheduleRepository repo;
    private final BranchRepository branchRepo;
    private final ScheduleMapper mapper;

    public ScheduleService(ScheduleRepository repo, BranchRepository branchRepo, ScheduleMapper mapper) {
        this.repo = repo;
        this.branchRepo = branchRepo;
        this.mapper = mapper;
    }

    private void ensureBranchExists(UUID branchId) {
        try { branchRepo.findById(branchId); }
        catch (Exception e) { throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Sucursal no encontrada: " + branchId); }
    }

    private void validateDay(int dayOfWeek) {
        if (dayOfWeek < 0 || dayOfWeek > 6) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "dayOfWeek debe estar entre 0 y 6");
    }

    private void validateTimes(Boolean closed, LocalTime open, LocalTime close) {
        if (closed == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "closed es obligatorio");
        if (!closed) {
            if (open == null || close == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Si closed=false, open y close son obligatorios");
            if (!open.isBefore(close)) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "open debe ser anterior a close");
        }
    }

    @Override
    @Transactional
    public ScheduleResponseDTO upsert(UUID branchId, ScheduleRequestDTO dto) {
        ensureBranchExists(branchId);
        int day = dto.dayOfWeek();
        validateDay(day);
        validateTimes(dto.closed(), dto.open(), dto.close());

        Schedule target;
        if (repo.existsByBranchAndDay(branchId, day)) {
            target = repo.findByBranchAndDay(branchId, day);
            target.setClosed(dto.closed());
            target.setOpen(dto.closed() ? null : dto.open());
            target.setClose(dto.closed() ? null : dto.close());
        } else {
            target = new Schedule(null, branchId, day,
                    dto.closed() ? null : dto.open(),
                    dto.closed() ? null : dto.close(),
                    dto.closed());
        }
        return mapper.toResponseDTO(repo.save(target));
    }

    @Override
    @Transactional
    public ScheduleResponseDTO update(UUID branchId, int dayOfWeek, ScheduleUpdateDTO dto) {
        ensureBranchExists(branchId);
        validateDay(dayOfWeek);

        Schedule current = repo.findByBranchAndDay(branchId, dayOfWeek);

        int newDay = dto.dayOfWeek() != null ? dto.dayOfWeek() : current.getDayOfWeek();
        validateDay(newDay);
        if (newDay != current.getDayOfWeek() && repo.existsByBranchAndDay(branchId, newDay)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe un horario para dayOfWeek=" + newDay);
        }

        Boolean closed = dto.closed() != null ? dto.closed() : current.isClosed();
        LocalTime open = dto.open() != null ? dto.open() : current.getOpen();
        LocalTime close = dto.close() != null ? dto.close() : current.getClose();
        validateTimes(closed, open, close);

        current.setDayOfWeek(newDay);
        current.setClosed(closed);
        current.setOpen(closed ? null : open);
        current.setClose(closed ? null : close);

        return mapper.toResponseDTO(repo.save(current));
    }

    @Override
    @Transactional(readOnly = true)
    public ScheduleResponseDTO getDay(UUID branchId, int dayOfWeek) {
        ensureBranchExists(branchId);
        validateDay(dayOfWeek);
        return mapper.toResponseDTO(repo.findByBranchAndDay(branchId, dayOfWeek));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ScheduleResponseDTO> getWeek(UUID branchId) {
        ensureBranchExists(branchId);
        return repo.findByBranch(branchId).stream()
                .sorted(Comparator.comparingInt(Schedule::getDayOfWeek))
                .map(mapper::toResponseDTO)
                .toList();
    }

    @Override
    @Transactional
    public void deleteDay(UUID branchId, int dayOfWeek) {
        ensureBranchExists(branchId);
        validateDay(dayOfWeek);
        repo.deleteByBranchAndDay(branchId, dayOfWeek);
    }

    @Override
    public ScheduleWeekResponseDTO upsertWeek(UUID branchId, ScheduleWeekRequestDTO dto) {
        ensureBranchExists(branchId);
        for (ScheduleRequestDTO dayDTO : dto.days()) {
            int day = dayDTO.dayOfWeek();
            validateDay(day);
            validateTimes(dayDTO.closed(), dayDTO.open(), dayDTO.close());

            Schedule target;
            if (repo.existsByBranchAndDay(branchId, day)) {
                target = repo.findByBranchAndDay(branchId, day);
                target.setClosed(dayDTO.closed());
                target.setOpen(dayDTO.closed() ? null : dayDTO.open());
                target.setClose(dayDTO.closed() ? null : dayDTO.close());
            } else {
                target = new Schedule(null, branchId, day,
                        dayDTO.closed() ? null : dayDTO.open(),
                        dayDTO.closed() ? null : dayDTO.close(),
                        dayDTO.closed());
            }
            repo.save(target);
        }
        return new ScheduleWeekResponseDTO(getWeek(branchId));
    }
}

