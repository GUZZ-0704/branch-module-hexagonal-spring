package com.example.sucursal_api.phone.application.mapper;

import com.example.sucursal_api.phone.adapter.out.BranchPhoneEntity;
import com.example.sucursal_api.phone.domain.BranchPhone;
import com.example.sucursal_api.phone.dto.BranchPhoneResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BranchPhoneMapper {

    @Mapping(target = "branchId", source = "branch.id")
    BranchPhone entityToDomain(BranchPhoneEntity entity);

    @Mapping(target = "branch", ignore = true)
    BranchPhoneEntity domainToEntity(BranchPhone domain);

    BranchPhoneResponseDTO toResponseDTO(BranchPhone domain);

    @Mapping(target = "branchId", source = "branch.id")
    List<BranchPhone> entitiesToDomains(List<BranchPhoneEntity> entities);
}
