package com.example.tickets.route;

import com.example.tickets.subscription.Subscription;

import java.util.List;

public interface RouteService {
    Route save(RouteDTO dto);

    List<RouteDTO> plan(Subscription subscription);
}
