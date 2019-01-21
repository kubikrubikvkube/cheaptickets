package com.example.tickets.route;

import com.example.tickets.subscription.Subscription;

import java.util.List;

public interface RoutePlanner {
    List<RouteDTO> plan(Subscription subscription);

}
