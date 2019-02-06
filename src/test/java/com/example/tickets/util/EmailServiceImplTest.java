package com.example.tickets.util;

import com.example.tickets.iata.IATARepository;
import com.example.tickets.owner.Owner;
import com.example.tickets.route.Route;
import com.example.tickets.ticket.Ticket;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class EmailServiceImplTest {
    @Autowired
    IATARepository iataRepository;

    @Autowired
    EmailService emailService;

    @Test
    void sendNotification() {
        Owner owner = new Owner();
        owner.setEmail("v.raskulin@gmail.com");

        Ticket departTicket = new Ticket();
        departTicket.setOrigin("IEV");
        departTicket.setDestination("OVB");
        departTicket.setDepartDate(LocalDate.now());
        departTicket.setNumberOfChanges(1);

        Ticket returnTicket = new Ticket();
        returnTicket.setOrigin("OVB");
        returnTicket.setDestination("IEV");
        returnTicket.setDepartDate(LocalDate.now().plusDays(2));
        returnTicket.setNumberOfChanges(0);

        Route route = new Route();
        route.setDepartTicket(departTicket);
        route.setReturnTicket(returnTicket);
        route.setTripDurationInDays(2);
        route.setOrigin("IEV");
        route.setDestination("OVB");
        route.setSumValue(123);

        emailService.sendNotification(owner, route);
    }
}