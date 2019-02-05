package com.example.tickets.notification;

import com.example.tickets.owner.Owner;
import com.example.tickets.route.Route;

import java.util.List;
import java.util.Optional;

public interface RouteNotificator {
    Optional<TicketNotification> notify(Owner owner, Route route);

    List<TicketNotification> notify(Owner owner, List<Route> routes);
}
