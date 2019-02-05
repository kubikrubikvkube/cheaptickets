package com.example.tickets.notification;

public interface RouteNotificationService {
    RouteNotification save(RouteNotificationDTO routeNotificationDTO);

    RouteNotification save(RouteNotification routeNotification);
}
