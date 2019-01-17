package com.example.tickets.owner;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface OwnerDTOMapper {
    OwnerDTOMapper INSTANCE = Mappers.getMapper(OwnerDTOMapper.class);

    OwnerDTO toDTO(Owner subscription);

    Owner fromDTO(OwnerDTO dto);
}
