package com.example.tickets;


import com.example.tickets.ticket.Ticket;
import com.example.tickets.ticket.TicketDTO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.modelmapper.spi.DestinationSetter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public ModelMapper defaultMapper() {
        ModelMapper modelMapper = new ModelMapper();
        TypeMap<TicketDTO, Ticket> ticketDTO_ticket_typeMap = modelMapper.typeMap(TicketDTO.class, Ticket.class);
        //TicketDTO <-> Ticket
        ticketDTO_ticket_typeMap.addMappings(mapper -> mapper.skip(Ticket::setId));
        ticketDTO_ticket_typeMap.addMappings(mapper -> mapper.skip(Ticket::setCatchedOn));
        ticketDTO_ticket_typeMap.addMapping(TicketDTO::getTrip_class, Ticket::setTripClass);
        ticketDTO_ticket_typeMap.addMapping(TicketDTO::getShow_to_affiliates, Ticket::setShowToAffiliates);
        ticketDTO_ticket_typeMap.addMapping(TicketDTO::getOrigin, Ticket::setOrigin);
        ticketDTO_ticket_typeMap.addMapping(TicketDTO::getDestination, Ticket::setDestination);
        ticketDTO_ticket_typeMap.addMapping(TicketDTO::getDepart_date, Ticket::setDepartDate);
        ticketDTO_ticket_typeMap.addMapping(TicketDTO::getDepartTime, Ticket::setDepartTime);
        ticketDTO_ticket_typeMap.addMapping(TicketDTO::getNumber_of_changes, Ticket::setNumberOfChanges);
        ticketDTO_ticket_typeMap.addMapping(TicketDTO::getValue, Ticket::setValue);
        ticketDTO_ticket_typeMap.addMapping(TicketDTO::getFound_at, Ticket::setFoundAt);
        ticketDTO_ticket_typeMap.addMapping(TicketDTO::getDistance, Ticket::setDistance);
        ticketDTO_ticket_typeMap.addMapping(TicketDTO::getActual, Ticket::setActual);
        ticketDTO_ticket_typeMap.addMapping(TicketDTO::getGate, Ticket::setGate);
        ticketDTO_ticket_typeMap.addMapping(TicketDTO::getAirline, Ticket::setAirline);
        ticketDTO_ticket_typeMap.addMapping(TicketDTO::getTransfers, Ticket::setTransfers);
        ticketDTO_ticket_typeMap.addMapping(TicketDTO::getIsExpired, Ticket::setIsExpired);
        ticketDTO_ticket_typeMap.addMapping(TicketDTO::getExpiresAt, Ticket::setExpiresAt);
        modelMapper.validate();
        // ObjectNode <-> TicketDTO
        TypeMap<ObjectNode, TicketDTO> objectNode_ticketDTO_typeMap = modelMapper.typeMap(ObjectNode.class, TicketDTO.class);
        objectNode_ticketDTO_typeMap.addMapping(source -> source.get("trip_class"),
                (DestinationSetter<TicketDTO, JsonNode>) (destination, value) -> destination.setTrip_class(value.asInt()));

        objectNode_ticketDTO_typeMap.addMapping(source -> source.get("origin"),
                (DestinationSetter<TicketDTO, JsonNode>) (destination, value) -> destination.setDestination(value.textValue()));
        objectNode_ticketDTO_typeMap.addMapping(source -> source.get("show_to_affiliates"),
                (DestinationSetter<TicketDTO, JsonNode>) (destination, value) -> destination.setShow_to_affiliates(value.booleanValue()));
        objectNode_ticketDTO_typeMap.addMapping(source -> source.get("distance"),
                (DestinationSetter<TicketDTO, JsonNode>) (destination, value) -> destination.setDistance(value.intValue()));
        //TODO доделать
        ticketDTO_ticket_typeMap.addMapping(TicketDTO::getShow_to_affiliates, Ticket::setShowToAffiliates);
        ticketDTO_ticket_typeMap.addMapping(TicketDTO::getOrigin, Ticket::setOrigin);
        ticketDTO_ticket_typeMap.addMapping(TicketDTO::getDestination, Ticket::setDestination);
        ticketDTO_ticket_typeMap.addMapping(TicketDTO::getDepart_date, Ticket::setDepartDate);
        ticketDTO_ticket_typeMap.addMapping(TicketDTO::getDepartTime, Ticket::setDepartTime);
        ticketDTO_ticket_typeMap.addMapping(TicketDTO::getNumber_of_changes, Ticket::setNumberOfChanges);
        ticketDTO_ticket_typeMap.addMapping(TicketDTO::getValue, Ticket::setValue);
        ticketDTO_ticket_typeMap.addMapping(TicketDTO::getFound_at, Ticket::setFoundAt);
        ticketDTO_ticket_typeMap.addMapping(TicketDTO::getDistance, Ticket::setDistance);
        ticketDTO_ticket_typeMap.addMapping(TicketDTO::getActual, Ticket::setActual);
        ticketDTO_ticket_typeMap.addMapping(TicketDTO::getGate, Ticket::setGate);
        ticketDTO_ticket_typeMap.addMapping(TicketDTO::getAirline, Ticket::setAirline);
        ticketDTO_ticket_typeMap.addMapping(TicketDTO::getTransfers, Ticket::setTransfers);
        ticketDTO_ticket_typeMap.addMapping(TicketDTO::getIsExpired, Ticket::setIsExpired);
        ticketDTO_ticket_typeMap.addMapping(TicketDTO::getExpiresAt, Ticket::setExpiresAt);
        objectNode_ticketDTO_typeMap.validate();
        return modelMapper;
        /*
    com.example.tickets.ticket.TicketDTO.setDepartTime()
	com.example.tickets.ticket.TicketDTO.setActual()
	com.example.tickets.ticket.TicketDTO.setIsExpired()
	com.example.tickets.ticket.TicketDTO.setGate()
	com.example.tickets.ticket.TicketDTO.setExpiresAt()
	com.example.tickets.ticket.TicketDTO.setDestination()
	com.example.tickets.ticket.TicketDTO.setAirline()
	com.example.tickets.ticket.TicketDTO.setTransfers()
	com.example.tickets.ticket.TicketDTO.setDepart_date()
	com.example.tickets.ticket.TicketDTO.setReturn_date()
	com.example.tickets.ticket.TicketDTO.setFlightNumber()
	com.example.tickets.ticket.TicketDTO.setTtl()
	com.example.tickets.ticket.TicketDTO.setCreated_at()
	com.example.tickets.ticket.TicketDTO.setFound_at()
	com.example.tickets.ticket.TicketDTO.setNumber_of_changes()
         */
    }
}
