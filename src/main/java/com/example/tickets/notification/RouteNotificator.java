package com.example.tickets.notification;

import com.example.tickets.route.Route;
import com.example.tickets.subscription.Subscription;

public interface RouteNotificator {
    RouteNotification notify(Subscription subscription, Route route);
}
