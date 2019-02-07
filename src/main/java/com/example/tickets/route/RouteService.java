package com.example.tickets.route;

import com.example.tickets.subscription.Subscription;

import java.util.List;

public interface RouteService {
    Route save(RouteDto dto);

    List<Route> save(List<RouteDto> routeDtos);

    List<Route> saveIfNotExist(List<RouteDto> routeDtos);

    List<Route> findBy(String origin, String destination);

    List<RouteDto> plan(Subscription subscription);

    List<Route> findBy(String origin, String destination, Integer limit);

    long count();
}
