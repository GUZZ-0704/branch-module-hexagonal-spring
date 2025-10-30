package com.example.sucursal_api.image.application.mapper;

import com.example.sucursal_api.image.adapter.out.BranchImageEntity;
import com.example.sucursal_api.image.domain.BranchImage;
import com.example.sucursal_api.image.dto.BranchImageResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BranchImageMapper {

    @Mapping(target = "branchId", source = "branch.id")
    BranchImage entityToDomain(BranchImageEntity entity);

    @Mapping(target = "branch", ignore = true)
    BranchImageEntity domainToEntity(BranchImage domain);

    BranchImageResponseDTO toResponseDTO(BranchImage domain);

    @Mapping(target = "branchId", source = "branch.id")
    List<BranchImage> entitiesToDomains(List<BranchImageEntity> entities);
}
