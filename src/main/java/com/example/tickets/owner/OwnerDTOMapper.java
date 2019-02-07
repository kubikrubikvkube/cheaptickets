package com.example.tickets.owner;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(componentModel = "spring", unmappedTargetPolicy = IGNORE)
public interface OwnerDTOMapper {
    OwnerDTOMapper INSTANCE = Mappers.getMapper(OwnerDTOMapper.class);

    OwnerDTO toDTO(Owner subscription);

    Owner fromDTO(OwnerDTO dto);
}
