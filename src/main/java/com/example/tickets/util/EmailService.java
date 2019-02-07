package com.example.tickets.util;

import com.example.tickets.owner.Owner;
import com.example.tickets.route.Route;

import java.util.Collection;

public interface EmailService {
    void sendNotifications(Owner owner, Collection<Route> routes);
}
