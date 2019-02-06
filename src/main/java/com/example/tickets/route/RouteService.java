package com.example.tickets.route;

import com.example.tickets.subscription.Subscription;

import java.util.List;

public interface RouteService {
    Route save(RouteDTO dto);

    List<Route> save(List<RouteDTO> routeDTOS);

    List<Route> saveIfNotExist(List<RouteDTO> routeDTOS);

    List<Route> findBy(String origin, String destination);

    List<RouteDTO> plan(Subscription subscription);

    List<Route> findBy(String origin, String destination, Integer limit);

    long count();
}
