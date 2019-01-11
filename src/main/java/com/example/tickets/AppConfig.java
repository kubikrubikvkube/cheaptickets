package com.example.tickets;


import com.example.tickets.ticket.Ticket;
import com.example.tickets.ticket.TicketDTO;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.modelmapper.convention.NameTokenizers;
import org.modelmapper.jackson.JsonNodeValueReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Bean
    public ModelMapper defaultMapper() {
        ModelMapper modelMapper = new ModelMapper();
        // JsonNode <-> Any
        modelMapper.getConfiguration().setSourceNameTokenizer(NameTokenizers.UNDERSCORE);
        modelMapper.getConfiguration().addValueReader(new JsonNodeValueReader());
        //http://modelmapper.org/user-manual/jackson-integration/

        //TicketDTO <-> Ticket
        TypeMap<TicketDTO, Ticket> ticketDTO_ticket_typeMap = modelMapper.typeMap(TicketDTO.class, Ticket.class);
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
        return modelMapper;

    }
}
