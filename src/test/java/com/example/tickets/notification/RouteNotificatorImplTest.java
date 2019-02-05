package com.example.tickets.notification;

import com.example.tickets.owner.Owner;
import com.example.tickets.owner.OwnerDTO;
import com.example.tickets.owner.OwnerService;
import com.example.tickets.route.Route;
import com.example.tickets.route.RouteDTO;
import com.example.tickets.route.RouteService;
import com.example.tickets.subscription.Subscription;
import com.example.tickets.subscription.SubscriptionDTO;
import com.example.tickets.subscription.SubscriptionService;
import com.example.tickets.subscription.SubscriptionType;
import com.example.tickets.ticket.Ticket;
import com.example.tickets.ticket.TicketDTO;
import com.example.tickets.ticket.TicketService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class RouteNotificatorImplTest {
    private final Logger log = LoggerFactory.getLogger(RouteNotificatorImplTest.class);
    @Autowired
    RouteService routesService;
    @Autowired
    OwnerService ownerService;
    @Autowired
    SubscriptionService subscriptionService;
    @Autowired
    TicketService ticketService;
    @Autowired
    RouteNotificationService routeNotificationService;
    @Autowired
    private RouteNotificator routeNotificator;
    private Owner owner;
    private Route route;
    private Subscription subscription;
    private RouteNotification routeNotification;

    @BeforeEach
    void setUp() {
        OwnerDTO ownerDTO = new OwnerDTO();
        ownerDTO.setEmail("v.raskulin@gmail.com");
        owner = ownerService.save(ownerDTO);

        RouteDTO routeDTO = new RouteDTO();
        routeDTO.setOrigin("LED");
        routeDTO.setDestination("MOW");
        route = routesService.save(routeDTO);

        TicketDTO departTicketDTO = new TicketDTO();
        departTicketDTO.setDepart_date(LocalDate.now());
        departTicketDTO.setNumber_of_changes(0);
        departTicketDTO.setOrigin("LED");
        departTicketDTO.setDestination("MOW");
        departTicketDTO.setValue(999);
        Ticket savedDepartTicket = ticketService.save(departTicketDTO);
        route.setDepartTicket(savedDepartTicket);

        TicketDTO returnTicketDTO = new TicketDTO();
        returnTicketDTO.setOrigin("MOW");
        returnTicketDTO.setDestination("LED");
        returnTicketDTO.setDepart_date(LocalDate.now().plusDays(2));
        returnTicketDTO.setNumber_of_changes(0);
        returnTicketDTO.setValue(1000);
        Ticket savedReturnTicket = ticketService.save(returnTicketDTO);
        route.setReturnTicket(savedReturnTicket);

        route.setTripDurationInDays(2);
        route.setSumValue(1999);

        RouteNotificationDTO routeNotificationDTO = new RouteNotificationDTO();
        routeNotification = routeNotificationService.save(routeNotificationDTO);
        routeNotification.setRoute(route);

        SubscriptionDTO subscriptionDTO = new SubscriptionDTO();
        subscriptionDTO.setOrigin("LED");
        subscriptionDTO.setDestination("MOW");
        subscriptionDTO.setSubscriptionType(SubscriptionType.DESTINATION_TRIP_DURATION_FROM_TRIP_DURATION_TO);
        subscriptionDTO.setTripDurationInDaysFrom(1);
        subscriptionDTO.setTripDurationInDaysTo(2);
        subscriptionDTO.setOwner(owner);
//        subscriptionDTO.setRouteNotifications(Collections.singletonList(routeNotification));

        subscription = subscriptionService.save(subscriptionDTO);
        routeNotification.setSubscription(subscription);
        routeNotificationService.save(routeNotification);

        owner.setSubscriptions(Collections.singletonList(subscription));
        ownerService.save(owner);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void notifyOwnerAndRoute() {
        Optional<RouteNotification> notifyOptional = routeNotificator.notify(subscription, route);
        assertTrue(notifyOptional.isPresent());
    }


    @Test
    void notifyOwnerAndRouteList() {
    }

    @Test
    void notify1() {
    }
}