package com.example.tickets.stages.stage;

import com.example.tickets.notification.RouteNotification;
import com.example.tickets.notification.RouteNotificationDto;
import com.example.tickets.notification.RouteNotificationDtoMapper;
import com.example.tickets.notification.RouteNotificationService;
import com.example.tickets.owner.Owner;
import com.example.tickets.owner.OwnerService;
import com.example.tickets.route.*;
import com.example.tickets.subscription.Subscription;
import com.example.tickets.subscription.SubscriptionService;
import com.example.tickets.util.EmailService;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.tickets.util.RouteComparators.WEIGHTED_SUM_MODEL;

@Component
public class RouteNotificationStage implements Stage {
    private static final Logger log = LoggerFactory.getLogger(RouteNotificationStage.class);
    private final OwnerService ownerService;
    private final RouteService routeService;
    private final RouteDtoMapper routeDtoMapper;
    private final SubscriptionService subscriptionService;
    private final RouteNotificationDtoMapper notificationDtoMapper;
    private final EmailService emailService;
    private final RouteNotificationService routeNotificationService;
    private final RoutePlanner routePlanner;

    public RouteNotificationStage(OwnerService ownerService, RouteService routeService, RouteDtoMapper routeDtoMapper, SubscriptionService subscriptionService, RouteNotificationDtoMapper notificationDtoMapper, EmailService emailService, RouteNotificationService routeNotificationService, RoutePlanner routePlanner) {
        this.ownerService = ownerService;
        this.routeService = routeService;
        this.routeDtoMapper = routeDtoMapper;
        this.subscriptionService = subscriptionService;
        this.notificationDtoMapper = notificationDtoMapper;
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
                    Route route = routeService.saveIfNotExist(routeDto);
                    routeNotificationDto.setRoute(route);

                    List<RouteNotificationDto> currentRouteNotificationDtos = subscription
                            .getRouteNotifications()
                            .stream()
                            .map(notificationDtoMapper::toDto)
                            .collect(Collectors.toList());

                    if (!currentRouteNotificationDtos.contains(routeNotificationDto)) {
                        RouteNotification saved = routeNotificationService.save(routeNotificationDto);
                        subscriptionService.addRouteNotification(saved, subscription);
                        ownerRouteNotifications.put(subscription, saved);
                    }
                }
            }


            if (!ownerRouteNotifications.values().isEmpty()) {
                log.info("Notifying owner {} about new cheap tickets", owner);
                emailService.sendNotifications(owner, ownerRouteNotifications.values());
            }
        }

        return new StageResult("RouteNotificationStage completed", 0, 0, 0);
    }
}
