package com.example.tickets.util;

import com.example.tickets.notification.RouteNotification;
import com.example.tickets.owner.Owner;

import java.util.Collection;

public interface EmailService {
    void sendNotifications(Owner owner, Collection<RouteNotification> routeNotifications);
}
