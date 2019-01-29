package com.example.tickets.job;

import com.example.tickets.route.RouteDTO;
import com.example.tickets.route.RoutesService;
import com.example.tickets.subscription.Subscription;
import com.example.tickets.subscription.SubscriptionService;
import com.google.common.base.Stopwatch;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.PersistJobDataAfterExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class RoutePlannerJob implements Job {
    private final Logger log = LoggerFactory.getLogger(RoutePlannerJob.class);
    private final RoutesService routesService;
    private final SubscriptionService subscriptionService;

    public RoutePlannerJob(RoutesService routesService, SubscriptionService subscriptionService) {
        this.routesService = routesService;
        this.subscriptionService = subscriptionService;
    }

    @Override
    public void execute(JobExecutionContext context) {
        Stopwatch timer = Stopwatch.createStarted();
        log.info("RoutePlannerJob started");

        Iterable<Subscription> subscriptions = subscriptionService.findAll();

        Multimap<Subscription, RouteDTO> all = ArrayListMultimap.create();

        for (Subscription subscription : subscriptions) {
            log.info("Calculating routes for {}", subscription);
            List<RouteDTO> routes = routesService.plan(subscription);
            log.info("Found {} routes for subscription {}", routes.size(), subscription);
            all.putAll(subscription, routes);
        }
        List<RouteDTO> routeDTOs = new ArrayList<>(all.values());
        routesService.save(routeDTOs);

        log.info("RoutePlannerStage finished in {}", timer.stop());
    }
}
