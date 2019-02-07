package com.example.tickets.ticket;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(componentModel = "spring", unmappedTargetPolicy = IGNORE)
public interface CheapTicketDtoMapper {
    TicketDtoMapper INSTANCE = Mappers.getMapper(TicketDtoMapper.class);

    Ticket toTicket(CheapTicket cheapTicket);

    CheapTicket toCheapTicket(Ticket ticket);

    List<Ticket> toTicket(List<CheapTicket> cheapTickets);

    List<CheapTicket> toCheapTicket(List<Ticket> tickets);
}
