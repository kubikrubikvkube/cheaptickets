package com.example.tickets.util;

import com.example.tickets.owner.Owner;
import com.example.tickets.route.Route;

public interface EmailService {
    void sendNotification(Owner owner, Route route);
}
