package com.example.tickets.route;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RouteDtoMapper {
    RouteDtoMapper INSTANCE = Mappers.getMapper(RouteDtoMapper.class);

    RouteDto toDto(Route route);

    Route fromDto(RouteDto dto);

    List<RouteDto> toDto(List<Route> route);

    List<Route> fromDto(List<RouteDto> dto);
}
