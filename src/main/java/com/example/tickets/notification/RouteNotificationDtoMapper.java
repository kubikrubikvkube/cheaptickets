package com.example.tickets.notification;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(componentModel = "spring", unmappedTargetPolicy = IGNORE)
public interface RouteNotificationDtoMapper {
    RouteNotificationDtoMapper INSTANCE = Mappers.getMapper(RouteNotificationDtoMapper.class);

    RouteNotificationDto toDto(RouteNotification notification);

    RouteNotification fromDto(RouteNotificationDto dto);
}
