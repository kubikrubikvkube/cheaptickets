package com.example.tickets;


import com.example.tickets.repository.Ticket;
import com.example.tickets.service.TicketDTO;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.boot.autoconfigure.quartz.SchedulerFactoryBeanCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import java.util.Properties;

@Configuration
public class AppConfig {

    @Bean
    @Lazy
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
        typeMap.addMapping(TicketDTO::getReturn_date, Ticket::setReturnDate);
        typeMap.addMapping(TicketDTO::getNumber_of_changes, Ticket::setNumberOfChanges);
        typeMap.addMapping(TicketDTO::getValue, Ticket::setValue);
        typeMap.addMapping(TicketDTO::getFound_at, Ticket::setFoundAt);
        typeMap.addMapping(TicketDTO::getDistance, Ticket::setDistance);
        typeMap.addMapping(TicketDTO::getActual, Ticket::setActual);
        typeMap.addMapping(TicketDTO::getGate, Ticket::setGate);
        typeMap.addMapping(TicketDTO::getAirline, Ticket::setAirline);
        typeMap.addMapping(TicketDTO::getTransfers, Ticket::setTransfers);
        modelMapper.validate();

        return modelMapper;
    }

    @Bean
    public SchedulerFactoryBeanCustomizer schedulerFactoryBeanCustomizer() {
        return new SchedulerFactoryBeanCustomizer() {
            @Override
            public void customize(SchedulerFactoryBean bean) {
                bean.setQuartzProperties(createQuartzProperties());
            }
        };
    }

    private Properties createQuartzProperties() {
        // Could also load from a file
        Properties props = new Properties();
        props.put("org.quartz.jobStore.driverDelegateClass", "org.quartz.impl.jdbcjobstore.PostgreSQLDelegate");
        return props;
    }
}
