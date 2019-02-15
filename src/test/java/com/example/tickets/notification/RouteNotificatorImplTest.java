package com.example.tickets.notification;

import com.example.tickets.owner.Owner;
import com.example.tickets.owner.OwnerDto;
import com.example.tickets.owner.OwnerService;
import com.example.tickets.route.Route;
import com.example.tickets.route.RouteDto;
import com.example.tickets.route.RouteService;
import com.example.tickets.subscription.Subscription;
import com.example.tickets.subscription.SubscriptionDto;
import com.example.tickets.subscription.SubscriptionService;
import com.example.tickets.subscription.SubscriptionType;
import com.example.tickets.ticket.Ticket;
import com.example.tickets.ticket.TicketDto;
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
    private static final Logger log = LoggerFactory.getLogger(RouteNotificatorImplTest.class);
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
        OwnerDto ownerDto = new OwnerDto();
        ownerDto.setEmail("v.raskulin@gmail.com");
        owner = ownerService.save(ownerDto);

        RouteDto routeDto = new RouteDto();
        routeDto.setOrigin("LED");
        routeDto.setDestination("MOW");
        route = routesService.save(routeDto);

        TicketDto departTicketDto = new TicketDto();
        departTicketDto.setDepartDate(LocalDate.now());
        departTicketDto.setNumberOfChanges(0);
        departTicketDto.setOrigin("LED");
        departTicketDto.setDestination("MOW");
        departTicketDto.setValue(999);
        Ticket savedDepartTicket = ticketService.save(departTicketDto);
        route.setDepartTicket(savedDepartTicket);

        TicketDto returnTicketDto = new TicketDto();
        returnTicketDto.setOrigin("MOW");
        returnTicketDto.setDestination("LED");
        returnTicketDto.setDepartDate(LocalDate.now().plusDays(2));
        returnTicketDto.setNumberOfChanges(0);
        returnTicketDto.setValue(1000);
        Ticket savedReturnTicket = ticketService.save(returnTicketDto);
        route.setReturnTicket(savedReturnTicket);

        route.setTripDurationInDays(2);
        route.setSumValue(1999);

        RouteNotificationDto routeNotificationDto = new RouteNotificationDto();
        routeNotification = routeNotificationService.save(routeNotificationDto);
        routeNotification.setRoute(route);

        SubscriptionDto subscriptionDto = new SubscriptionDto();
        subscriptionDto.setOrigin("LED");
        subscriptionDto.setDestination("MOW");
        subscriptionDto.setSubscriptionType(SubscriptionType.DESTINATION_TRIP_DURATION_FROM_TRIP_DURATION_TO);
        subscriptionDto.setTripDurationInDaysFrom(1);
        subscriptionDto.setTripDurationInDaysTo(2);
        subscriptionDto.setOwner(owner);
//        subscriptionDto.setRouteNotifications(Collections.singletonList(routeNotification));

        subscription = subscriptionService.save(subscriptionDto);
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