package com.example.tickets.route;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface RouteDTOMapper {
    RouteDTOMapper INSTANCE = Mappers.getMapper(RouteDTOMapper.class);

    RouteDTO routeToDTO(Route route);

    Route dtoToRoute(RouteDTO dto);
}
