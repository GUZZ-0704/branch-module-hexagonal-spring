package com.example.sucursal_api.branch.application.mapper;


import com.example.sucursal_api.branch.adapter.out.BranchEntity;
import com.example.sucursal_api.branch.domain.Branch;
import com.example.sucursal_api.branch.dto.BranchResponseDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BranchMapper {
    Branch entityToDomain(BranchEntity entity);
    BranchEntity domainToEntity(Branch domain);

    BranchResponseDTO toResponseDTO(Branch domain);
    List<Branch> entitiesToDomains(List<BranchEntity> entities);
}