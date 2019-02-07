package com.example.tickets.ticket;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(componentModel = "spring", unmappedTargetPolicy = IGNORE)
public interface TicketDTOMapper {

    TicketDTOMapper INSTANCE = Mappers.getMapper(TicketDTOMapper.class);

    TicketDTO toDTO(Ticket source);

    Ticket fromDTO(TicketDTO destination);
}