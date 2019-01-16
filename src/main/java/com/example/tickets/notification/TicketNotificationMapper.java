package com.example.tickets.notification;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface TicketNotificationMapper {
    TicketNotificationMapper INSTANCE = Mappers.getMapper(TicketNotificationMapper.class);

    TicketNotificationDTO ticketNotificationToDTO(TicketNotification notification);

    TicketNotification dtoToTicketNotification(TicketNotificationDTO dto);
}
