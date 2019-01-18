package com.example.tickets.notification;

import com.example.tickets.route.Route;
import com.example.tickets.ticket.Ticket;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
class TicketNotificationTest {
    private Ticket departTicket;
    private Ticket returnTicket;

    @BeforeEach
    void setUp() {
        departTicket = new Ticket();
        departTicket.setOrigin("led");
        departTicket.setDestination("mow");

        returnTicket = new Ticket();
        returnTicket.setOrigin("mow");
        returnTicket.setDestination("led");
    }

    @Test
    void simplestNotificationShouldBeCreated() throws JsonProcessingException {
        TicketNotification notification = new TicketNotification();
        Route route = new Route();
        route.setDepartTicket(departTicket);
        route.setReturnTicket(returnTicket);
        route.setOrigin("led");
        route.setDestination("mow");
        route.setSumValue(1000);
        route.setTripDurationInDays(5);
        notification.setOwner("test");
        notification.setRoute(route);
        ObjectMapper mapper = new ObjectMapper();
        String notificationJSON = mapper.writeValueAsString(notification);
        String routeJSON = mapper.writeValueAsString(route);
        assertNotNull(notificationJSON);
        assertNotNull(routeJSON);
        //TODO добавить ассёрты на поля
    }
}