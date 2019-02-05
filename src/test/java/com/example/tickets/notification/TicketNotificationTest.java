package com.example.tickets.notification;

import com.example.tickets.owner.Owner;
import com.example.tickets.route.Route;
import com.example.tickets.route.RouteRepository;
import com.example.tickets.subscription.Subscription;
import com.example.tickets.subscription.SubscriptionRepository;
import com.example.tickets.ticket.Ticket;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class TicketNotificationTest {
    private Ticket departTicket;
    private Ticket returnTicket;

    private final Logger log = LoggerFactory.getLogger(TicketNotificationTest.class);
    @Autowired
    private TicketNotificationRepository ticketNotificationRepository;
    @Autowired
    private SubscriptionRepository subscriptionRepository;
    @Autowired
    private RouteRepository routeRepository;

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
        Owner owner = new Owner();
        owner.setId(1L);
        owner.setEmail("localhost@mail.ru");
        owner.setSubscriptions(Collections.emptyList());
        owner.setCreationTimestamp(LocalDateTime.now());
        notification.setRoute(route);
        ObjectMapper mapper = new ObjectMapper();
        String notificationJSON = mapper.writeValueAsString(notification);
        String routeJSON = mapper.writeValueAsString(route);
        assertNotNull(notificationJSON);
        assertNotNull(routeJSON);
        //TODO добавить ассёрты на поля
    }

    @Test
    void simplestNotificationShouldBeSaved() throws JsonProcessingException {
        TicketNotification ticketNotification = new TicketNotification();
        Subscription subscription = new Subscription();
        subscription.setOrigin("LED");
        subscription.setDestination("MOW");
        subscription = subscriptionRepository.save(subscription);
        log.info("Subscription: {}", subscription);
        ticketNotification.setSubscription(subscription);
        Route route = new Route();
        route.setOrigin("LED");
        route.setDestination("MOW");
        route.setTripDurationInDays(1);
        route.setSumValue(1000);
        route = routeRepository.save(route);
        log.info("Route: {}", route);
        ticketNotification.setRoute(route);
        TicketNotification saved = ticketNotificationRepository.save(ticketNotification);
        log.info("TicketNotification: {}", ticketNotification);
        assertNotNull(saved.getId());
        assertNotNull(saved.getCreationTimestamp());
        assertNotNull(saved.getRoute());
        assertNotNull(saved.getSubscription());
    }

}