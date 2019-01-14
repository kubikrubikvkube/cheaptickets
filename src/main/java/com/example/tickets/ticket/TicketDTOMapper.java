package com.example.tickets.ticket;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface TicketDTOMapper {

    TicketDTOMapper INSTANCE = Mappers.getMapper(TicketDTOMapper.class);

    @Mappings({
            @Mapping(source = "tripClass", target = "trip_class"),
            @Mapping(source = "showToAffiliates", target = "show_to_affiliates"),
            @Mapping(source = "departDate", target = "depart_date"),
            @Mapping(source = "returnDate", target = "return_date"),
            @Mapping(source = "departTime", target = "depart_time"),
            @Mapping(source = "numberOfChanges", target = "number_of_changes"),
            @Mapping(source = "foundAt", target = "found_at"),
            @Mapping(source = "flightNumber", target = "flight_number"),
            @Mapping(source = "expiresAt", target = "expires_at"),
            @Mapping(source = "createdAt", target = "created_at"),
            @Mapping(source = "isExpired", target = "is_expired")
    })
    TicketDTO ticketToDTO(Ticket source);

    @Mappings({
            @Mapping(source = "trip_class", target = "tripClass"),
            @Mapping(source = "show_to_affiliates", target = "showToAffiliates"),
            @Mapping(source = "depart_date", target = "departDate"),
            @Mapping(source = "return_date", target = "returnDate"),
            @Mapping(source = "depart_time", target = "departTime"),
            @Mapping(source = "number_of_changes", target = "numberOfChanges"),
            @Mapping(source = "found_at", target = "foundAt"),
            @Mapping(source = "flight_number", target = "flightNumber"),
            @Mapping(source = "expires_at", target = "expiresAt"),
            @Mapping(source = "created_at", target = "createdAt"),
            @Mapping(source = "is_expired", target = "isExpired")
    })
    Ticket dtoToTicket(TicketDTO destination);
}