package com.example.tickets;


import com.example.tickets.ticket.Ticket;
import com.example.tickets.ticket.TicketDTO;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public ModelMapper defaultMapper() {
        ModelMapper modelMapper = new ModelMapper();
        TypeMap<TicketDTO, Ticket> typeMap = modelMapper.typeMap(TicketDTO.class, Ticket.class);
        //TicketDTO <-> Ticket
        typeMap.addMappings(mapper -> mapper.skip(Ticket::setId));
        typeMap.addMappings(mapper -> mapper.skip(Ticket::setCatchedOn));
        typeMap.addMapping(TicketDTO::getTrip_class, Ticket::setTripClass);
        typeMap.addMapping(TicketDTO::getShow_to_affiliates, Ticket::setShowToAffiliates);
        typeMap.addMapping(TicketDTO::getOrigin, Ticket::setOrigin);
        typeMap.addMapping(TicketDTO::getDestination, Ticket::setDestination);
        typeMap.addMapping(TicketDTO::getDepart_date, Ticket::setDepartDate);
        typeMap.addMapping(TicketDTO::getDepartTime, Ticket::setDepartTime);
        typeMap.addMapping(TicketDTO::getNumber_of_changes, Ticket::setNumberOfChanges);
        typeMap.addMapping(TicketDTO::getValue, Ticket::setValue);
        typeMap.addMapping(TicketDTO::getFound_at, Ticket::setFoundAt);
        typeMap.addMapping(TicketDTO::getDistance, Ticket::setDistance);
        typeMap.addMapping(TicketDTO::getActual, Ticket::setActual);
        typeMap.addMapping(TicketDTO::getGate, Ticket::setGate);
        typeMap.addMapping(TicketDTO::getAirline, Ticket::setAirline);
        typeMap.addMapping(TicketDTO::getTransfers, Ticket::setTransfers);
        typeMap.addMapping(TicketDTO::getIsExpired, Ticket::setIsExpired);
        typeMap.addMapping(TicketDTO::getExpiresAt, Ticket::setExpiresAt);
        modelMapper.validate();
        return modelMapper;
    }
}
