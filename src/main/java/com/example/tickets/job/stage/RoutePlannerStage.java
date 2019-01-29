package com.example.tickets.job.stage;

import com.example.tickets.route.Route;
import com.example.tickets.route.RouteDTO;
import com.example.tickets.route.RoutesService;
import com.example.tickets.subscription.Subscription;
import com.example.tickets.subscription.SubscriptionService;
import com.google.common.base.Stopwatch;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class RoutePlannerStage implements Stage {
    private final static Logger log = LoggerFactory.getLogger(RoutePlannerStage.class);
    private final RoutesService routesService;
    private final SubscriptionService subscriptionService;

    public RoutePlannerStage(RoutesService routesService, SubscriptionService subscriptionService) {
        this.routesService = routesService;
        this.subscriptionService = subscriptionService;
    }

    @Override
    public StageResult call() {
        Stopwatch timer = Stopwatch.createStarted();
        log.info("RoutePlannerStage started");

        Iterable<Subscription> subscriptions = subscriptionService.findAll();

        Multimap<Subscription, RouteDTO> all = ArrayListMultimap.create();

        for (Subscription subscription : subscriptions) {
            log.info("Calculating routes for {}", subscription);
            List<RouteDTO> routes = routesService.plan(subscription);
            log.info("Found {} routes for subscription {}", routes.size(), subscription);
            all.putAll(subscription, routes);
        }
        List<RouteDTO> routeDTOs = new ArrayList<>(all.values());
        List<Route> savedRoutes = routesService.saveIfNotExist(routeDTOs);

        log.info("RoutePlannerStage finished in {}", timer.stop());
        return new StageResult("RoutePlannerStage", savedRoutes.size(), 0, 0);
    }
}
