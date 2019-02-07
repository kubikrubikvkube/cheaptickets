package com.example.tickets.owner;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(componentModel = "spring", unmappedTargetPolicy = IGNORE)
public interface OwnerDtoMapper {
    OwnerDtoMapper INSTANCE = Mappers.getMapper(OwnerDtoMapper.class);

    OwnerDto toDto(Owner subscription);

    Owner fromDto(OwnerDto dto);
}
