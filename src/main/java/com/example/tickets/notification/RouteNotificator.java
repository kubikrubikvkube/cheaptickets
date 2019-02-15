package com.example.tickets.notification;

import com.example.tickets.route.Route;
import com.example.tickets.subscription.Subscription;

import java.util.List;
import java.util.Optional;

public interface RouteNotificator {
    Optional<RouteNotification> notify(Subscription subscription, Route route);
    List<RouteNotification> notify(Subscription subscription, List<Route> routes);
}
