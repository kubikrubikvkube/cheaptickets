package com.example.tickets.iata;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IATADTOMapper {
    IATADTOMapper INSTANCE = Mappers.getMapper(IATADTOMapper.class);

    IATADTO toDTO(IATA route);

    IATA fromDTO(IATADTO dto);

    List<IATADTO> toDTO(List<IATA> iata);

    List<IATA> fromDTO(List<IATADTO> dto);
}
