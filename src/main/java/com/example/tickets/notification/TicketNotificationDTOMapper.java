package com.example.tickets.notification;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface TicketNotificationDTOMapper {
    TicketNotificationDTOMapper INSTANCE = Mappers.getMapper(TicketNotificationDTOMapper.class);

    TicketNotificationDTO toDTO(TicketNotification notification);

    TicketNotification fromDTO(TicketNotificationDTO dto);
}
