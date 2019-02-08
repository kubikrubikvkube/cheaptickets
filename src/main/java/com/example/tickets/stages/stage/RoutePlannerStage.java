package com.example.tickets.stages.stage;

import com.example.tickets.route.RouteService;
import com.example.tickets.subscription.SubscriptionService;
import com.google.common.base.Stopwatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class RoutePlannerStage implements Stage {
    private final static Logger log = LoggerFactory.getLogger(RoutePlannerStage.class);
    private final RouteService routesService;
    private final SubscriptionService subscriptionService;

    public RoutePlannerStage(RouteService routesService, SubscriptionService subscriptionService) {
        this.routesService = routesService;
        this.subscriptionService = subscriptionService;
    }

    @Override
    public StageResult call() {
        Stopwatch timer = Stopwatch.createStarted();
        log.info("RoutePlannerStage started");

//        Iterable<Subscription> subscriptions = subscriptionService.findAll();
//
//        Multimap<Subscription, RouteDto> all = ArrayListMultimap.create();
//
//        for (Subscription subscription : subscriptions) {
//            log.info("Calculating routes for {}", subscription);
//            List<RouteDto> routes = routesService.plan(subscription);
//            log.info("Found {} routes for subscription {}", routes.size(), subscription);
//            all.putAll(subscription, routes);
//        }
//        List<RouteDto> routeDtos = new ArrayList<>(all.values());
//        List<Route> savedRoutes = routesService.saveIfNotExist(routeDtos);

        log.info("RoutePlannerStage finished in {}", timer.stop());
        return new StageResult("RoutePlannerStage", 0, 0, 0);
    }
}
