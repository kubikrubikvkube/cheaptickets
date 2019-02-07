package com.example.tickets.ticket;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(componentModel = "spring", unmappedTargetPolicy = IGNORE)
public interface TicketDtoMapper {

    TicketDtoMapper INSTANCE = Mappers.getMapper(TicketDtoMapper.class);

    TicketDto toDto(Ticket source);

    Ticket fromDto(TicketDto destination);
}