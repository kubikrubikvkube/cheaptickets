package com.example.tickets.stages.stage;

import com.example.tickets.notification.RouteNotification;
import com.example.tickets.notification.RouteNotificationDto;
import com.example.tickets.notification.RouteNotificationService;
import com.example.tickets.owner.Owner;
import com.example.tickets.owner.OwnerService;
import com.example.tickets.route.RouteDto;
import com.example.tickets.route.RouteDtoMapper;
import com.example.tickets.route.RoutePlanner;
import com.example.tickets.route.RouteService;
import com.example.tickets.subscription.Subscription;
import com.example.tickets.util.EmailService;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.example.tickets.util.RouteComparators.WEIGHTED_SUM_MODEL;

@Component
public class RouteNotificationStage implements Stage {
    private final OwnerService ownerService;
    private final RouteService routeService;
    private final RouteDtoMapper routeDtoMapper;
    private final EmailService emailService;
    private final RouteNotificationService routeNotificationService;
    private final Logger log = LoggerFactory.getLogger(RouteNotificationStage.class);
    private final RoutePlanner routePlanner;

    public RouteNotificationStage(OwnerService ownerService, RouteService routeService, RouteDtoMapper routeDtoMapper, EmailService emailService, RouteNotificationService routeNotificationService, RoutePlanner routePlanner) {
        this.ownerService = ownerService;
        this.routeService = routeService;
        this.routeDtoMapper = routeDtoMapper;
        this.emailService = emailService;
        this.routeNotificationService = routeNotificationService;
        this.routePlanner = routePlanner;
    }

    @Override
    public StageResult call() {
        for (Owner owner : ownerService.findAll()) {
            log.info("Processing owner {}", owner);
            Multimap<Subscription, RouteNotification> ownerRouteNotifications = Multimaps.synchronizedListMultimap(ArrayListMultimap.create());
            List<Subscription> ownerSubscriptions = owner.getSubscriptions();

            for (Subscription subscription : ownerSubscriptions) {
                log.info("Processing subscription {}", subscription);
                List<RouteDto> routeDtos = routePlanner.plan(subscription);
                log.info("Planned {} routes", routeDtos.size());
                routeDtos.sort(WEIGHTED_SUM_MODEL);
                log.debug("Routes sorting completed");
                routeService.saveIfNotExist(routeDtos);

                Iterable<RouteDto> topThreeRoutes = Iterables.limit(routeDtos, 3);
                for (RouteDto routeDto : topThreeRoutes) {
                    log.debug("Processing one of top 3 routes {}", routeDto);
                    RouteNotificationDto routeNotificationDto = new RouteNotificationDto();
                    routeNotificationDto.setRoute(routeService.save(routeDto));
                    routeNotificationDto.setSubscription(subscription);
                    RouteNotification saved = routeNotificationService.save(routeNotificationDto);
                    ownerRouteNotifications.put(subscription, saved);
                }

            }

            log.info("Notifying owner {} about new cheap tickets", owner);
            emailService.sendNotifications(owner, ownerRouteNotifications.values());
        }

        return new StageResult("RouteNotificationStage completed", 0, 0, 0);
    }
}
