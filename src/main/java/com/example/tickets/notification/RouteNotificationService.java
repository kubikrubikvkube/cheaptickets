package com.example.tickets.notification;

public interface RouteNotificationService {
    RouteNotification save(RouteNotificationDto routeNotificationDto);

    RouteNotification save(RouteNotification routeNotification);
}
