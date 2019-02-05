package com.example.tickets.notification;

import com.example.tickets.owner.Owner;
import com.example.tickets.route.Route;
import com.example.tickets.subscription.Subscription;
import com.example.tickets.subscription.SubscriptionType;
import com.example.tickets.ticket.Ticket;
import org.junit.jupiter.api.AfterEach;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class RouteNotificatorImplTest {
    private final Logger log = LoggerFactory.getLogger(RouteNotificatorImplTest.class);

    @Autowired
    private RouteNotificator routeNotificator;

    private Owner owner;
    private Route route;
    private Subscription subscription;
    private RouteNotification routeNotification;

    @BeforeEach
    void setUp() {
        owner = new Owner();
        owner.setId(1L);
        owner.setCreationTimestamp(LocalDateTime.now());
        owner.setEmail("test@example.com");


        route = new Route();
        route.setOrigin("LED");
        route.setDestination("MOW");
        route.setDepartTicket(new Ticket());
        route.setReturnTicket(new Ticket());
        route.setSumValue(1000);

        routeNotification = new RouteNotification();
        routeNotification.setId(1L);
        routeNotification.setCreationTimestamp(LocalDateTime.now());
        routeNotification.setRoute(route);

        subscription = new Subscription();
        subscription.setId(1L);
        subscription.setOrigin("LED");
        subscription.setDestination("MOW");
        subscription.setSubscriptionType(SubscriptionType.DESTINATION_TRIP_DURATION_FROM_TRIP_DURATION_TO);
        subscription.setTripDurationInDaysFrom(1);
        subscription.setTripDurationInDaysTo(2);
        subscription.setOwner(owner);
        subscription.setRouteNotifications(Collections.singletonList(routeNotification));


        routeNotification.setSubscription(subscription);

        owner.setSubscriptions(Collections.singletonList(subscription));
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