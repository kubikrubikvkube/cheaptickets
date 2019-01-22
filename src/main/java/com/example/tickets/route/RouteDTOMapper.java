package com.example.tickets.route;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RouteDTOMapper {
    RouteDTOMapper INSTANCE = Mappers.getMapper(RouteDTOMapper.class);

    RouteDTO toDTO(Route route);

    Route fromDTO(RouteDTO dto);

    List<RouteDTO> toDTO(List<Route> route);

    List<Route> fromDTO(List<RouteDTO> dto);
}
