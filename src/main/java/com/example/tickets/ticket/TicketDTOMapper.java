package com.example.tickets.ticket;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface TicketDTOMapper {

    TicketDTOMapper INSTANCE = Mappers.getMapper(TicketDTOMapper.class);

    TicketDTO ticketToDTO(Ticket source);

    Ticket dtoToTicket(TicketDTO destination);
}