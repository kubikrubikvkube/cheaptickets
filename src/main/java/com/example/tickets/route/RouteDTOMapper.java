package com.example.tickets.route;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface RouteDTOMapper {
    RouteDTOMapper INSTANCE = Mappers.getMapper(RouteDTOMapper.class);

    RouteDTO toDTO(Route route);

    Route fromDTO(RouteDTO dto);
}
