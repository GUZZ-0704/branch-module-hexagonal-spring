package com.example.sucursal_api.phone.application;


import com.example.sucursal_api.branch.port.out.BranchRepository;
import com.example.sucursal_api.phone.application.mapper.BranchPhoneMapper;
import com.example.sucursal_api.phone.domain.BranchPhone;
import com.example.sucursal_api.phone.domain.PhoneKind;
import com.example.sucursal_api.phone.domain.PhoneState;
import com.example.sucursal_api.phone.dto.BranchPhoneRequestDTO;
import com.example.sucursal_api.phone.dto.BranchPhoneResponseDTO;
import com.example.sucursal_api.phone.dto.BranchPhoneUpdateDTO;
import com.example.sucursal_api.phone.port.in.BranchPhoneUseCase;
import com.example.sucursal_api.phone.port.out.BranchPhoneRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;



@Service
public class BranchPhoneService implements BranchPhoneUseCase {

    private final BranchPhoneRepository repo;
    private final BranchRepository branchRepo;
    private final BranchPhoneMapper mapper;

    public BranchPhoneService(BranchPhoneRepository repo, BranchRepository branchRepo, BranchPhoneMapper mapper) {
        this.repo = repo;
        this.branchRepo = branchRepo;
        this.mapper = mapper;
    }

    private void ensureBranchExists(UUID branchId) {
        try { branchRepo.findById(branchId); }
        catch (Exception e) { throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Sucursal no encontrada: " + branchId); }
    }

    private String normalizeNumber(String raw) {
        return raw == null ? null : raw.trim().replaceAll("\\s+", " ");
    }

    private void validateKindState(PhoneKind kind, PhoneState state) {
        if (kind == PhoneKind.CORPORATE && state == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Para kind=CORPORATE, 'state' es obligatorio.");
        }
    }

    @Override
    @Transactional
    public BranchPhoneResponseDTO create(UUID branchId, BranchPhoneRequestDTO dto) {
        ensureBranchExists(branchId);

        String number = normalizeNumber(dto.number());
        if (repo.existsByBranchAndNumber(branchId, number)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe un teléfono con ese número en la sucursal.");
        }
        System.out.println("Branch Id" + branchId);

        PhoneKind kind = dto.kind();
        PhoneState state = dto.state();
        validateKindState(kind, state);
        if (kind == PhoneKind.PUBLIC) state = null;

        BranchPhone phone = new BranchPhone(
                null, branchId, number, kind, state,
                dto.label(),
                dto.whatsapp() != null && dto.whatsapp(),
                dto.publish() != null ? dto.publish() : (kind == PhoneKind.PUBLIC),
                dto.priority() != null ? dto.priority() : 0
        );

        return mapper.toResponseDTO(repo.save(phone));
    }

    @Override
    @Transactional(readOnly = true)
    public BranchPhoneResponseDTO getById(UUID branchId, UUID phoneId) {
        ensureBranchExists(branchId);
        BranchPhone p = repo.findById(phoneId);
        if (!p.getBranchId().equals(branchId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El teléfono no pertenece a la sucursal especificada.");
        }
        return mapper.toResponseDTO(p);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BranchPhoneResponseDTO> listByBranch(UUID branchId, PhoneKind kind) {
        ensureBranchExists(branchId);
        var list = (kind == null) ? repo.findByBranch(branchId)
                : repo.findByBranchAndKind(branchId, kind);
        return list.stream().map(mapper::toResponseDTO).toList();
    }

    @Override
    @Transactional
    public BranchPhoneResponseDTO update(UUID branchId, UUID phoneId, BranchPhoneUpdateDTO dto) {
        ensureBranchExists(branchId);

        BranchPhone current = repo.findById(phoneId);
        if (!current.getBranchId().equals(branchId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El teléfono no pertenece a la sucursal especificada.");
        }

        if (dto.number() != null) {
            String newNumber = normalizeNumber(dto.number());
            if (!newNumber.equals(current.getNumber()) && repo.existsByBranchAndNumber(branchId, newNumber)) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe un teléfono con ese número en la sucursal.");
            }
            current.setNumber(newNumber);
        }

        PhoneKind newKind = dto.kind() != null ? dto.kind() : current.getKind();
        PhoneState newState = dto.state() != null ? dto.state() : current.getState();
        validateKindState(newKind, newState);
        if (newKind == PhoneKind.PUBLIC) newState = null;

        current.setKind(newKind);
        current.setState(newState);

        if (dto.label() != null) current.setLabel(dto.label());
        if (dto.whatsapp() != null) current.setWhatsapp(dto.whatsapp());
        if (dto.publish() != null) current.setPublish(dto.publish());
        if (dto.priority() != null) current.setPriority(dto.priority());

        return mapper.toResponseDTO(repo.save(current));
    }

    @Override
    @Transactional
    public void delete(UUID branchId, UUID phoneId) {
        ensureBranchExists(branchId);
        try {
            BranchPhone current = repo.findById(phoneId);
            if (!current.getBranchId().equals(branchId)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El teléfono no pertenece a la sucursal especificada.");
            }
            repo.delete(phoneId);
        } catch (java.util.NoSuchElementException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        } catch (org.springframework.dao.DataIntegrityViolationException ex) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "No se puede eliminar el teléfono porque está en uso o tiene referencias activas.");
        } catch (Exception ex) {
            // Cualquier otro error persistente se traduce como conflicto para el cliente
            throw new ResponseStatusException(HttpStatus.CONFLICT, "No se pudo eliminar el teléfono: " + ex.getMessage());
        }
    }
}
