package com.example.tickets.route;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RouteController {
    private final RouteService routeService;

    public RouteController(RouteService routeService) {
        this.routeService = routeService;
    }


    @RequestMapping(value = "/route/get", params = {"origin", "destination"})
    public List<Route> get(@RequestParam String origin, @RequestParam String destination) {
        return routeService.findBy(origin, destination);
    }

    @RequestMapping(value = "/route/get", params = {"origin", "destination", "limit"})
    public List<Route> get(@RequestParam String origin, @RequestParam String destination, @RequestParam String limit) {
        return routeService.findBy(origin, destination, limit);
    }
}
