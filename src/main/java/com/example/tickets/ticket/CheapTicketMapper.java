package com.example.tickets.ticket;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CheapTicketMapper {
    TicketDTOMapper INSTANCE = Mappers.getMapper(TicketDTOMapper.class);
    Ticket toTicket(CheapTicket cheapTicket);
    CheapTicket toCheapTicket(Ticket ticket);

    List<Ticket> toTicket(List<CheapTicket> cheapTickets);

    List<CheapTicket> toCheapTicket(List<Ticket> tickets);
}
