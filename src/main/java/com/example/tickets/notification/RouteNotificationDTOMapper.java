package com.example.tickets.notification;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface RouteNotificationDTOMapper {
    RouteNotificationDTOMapper INSTANCE = Mappers.getMapper(RouteNotificationDTOMapper.class);

    RouteNotificationDTO toDTO(RouteNotification notification);

    RouteNotification fromDTO(RouteNotificationDTO dto);
}
