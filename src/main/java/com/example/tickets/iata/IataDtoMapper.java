package com.example.tickets.iata;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(componentModel = "spring", unmappedTargetPolicy = IGNORE)
public interface IataDtoMapper {
    IataDtoMapper INSTANCE = Mappers.getMapper(IataDtoMapper.class);

    IataDto toDto(Iata route);

    Iata fromDto(IataDto dto);

    List<IataDto> toDto(List<Iata> iata);

    List<Iata> fromDto(List<IataDto> dto);
}