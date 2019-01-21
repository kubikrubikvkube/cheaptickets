package com.example.tickets.job;

import com.example.tickets.route.RouteDTO;
import com.example.tickets.route.RouteService;
import com.example.tickets.subscription.Subscription;
import com.example.tickets.subscription.SubscriptionService;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.List;

import static java.lang.String.format;

@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class RoutePlannerJob implements Job {
    private final Logger log = LoggerFactory.getLogger(RoutePlannerJob.class);
    private final RouteService routeService;
    private final SubscriptionService subscriptionService;

    public RoutePlannerJob(RouteService routeService, SubscriptionService subscriptionService) {
        this.routeService = routeService;
        this.subscriptionService = subscriptionService;
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        var startTime = Instant.now().toEpochMilli();
        log.info("RoutePlannerJob started");

        Iterable<Subscription> subscriptions = subscriptionService.findAll();

        Multimap<Subscription, RouteDTO> all = ArrayListMultimap.create();
        for (Subscription subscription : subscriptions) {
            List<RouteDTO> routes = routeService.plan(subscription);
            all.putAll(subscription, routes);
        }
        var g = 0;

        var endTime = Instant.now().toEpochMilli();
        log.info(format("RoutePlannerJob finished in %d ms", endTime - startTime));
    }
}
