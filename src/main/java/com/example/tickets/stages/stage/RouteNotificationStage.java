package com.example.tickets.stages.stage;

import com.example.tickets.notification.RouteNotification;
import com.example.tickets.notification.RouteNotificationDto;
import com.example.tickets.notification.RouteNotificationDtoMapper;
import com.example.tickets.notification.RouteNotificationService;
import com.example.tickets.owner.Owner;
import com.example.tickets.owner.OwnerService;
import com.example.tickets.route.Route;
import com.example.tickets.route.RouteService;
import com.example.tickets.subscription.Subscription;
import com.example.tickets.util.EmailService;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.tickets.util.RouteComparators.WEIGHTED_SUM_MODEL;

@Component
public class RouteNotificationStage implements Stage {
    private final OwnerService ownerService;
    private final RouteService routeService;
    private final RouteNotificationDtoMapper routeDtoMapper;
    private final EmailService emailService;
    private final RouteNotificationService routeNotificationService;
    private final Logger log = LoggerFactory.getLogger(RouteNotificationStage.class);

    public RouteNotificationStage(OwnerService ownerService, RouteService routeService, RouteNotificationDtoMapper routeDtoMapper, EmailService emailService, RouteNotificationService routeNotificationService) {
        this.ownerService = ownerService;
        this.routeService = routeService;
        this.routeDtoMapper = routeDtoMapper;
        this.emailService = emailService;
        this.routeNotificationService = routeNotificationService;
    }

    @Override
    public StageResult call() {
        List<Owner> allOwners = ownerService.findAll();

        for (Owner owner : allOwners) {
            Multimap<Subscription, RouteNotification> ownerRouteNotifications = ArrayListMultimap.create();
            List<Subscription> ownerSubscriptions = owner.getSubscriptions();
            for (Subscription subscription : ownerSubscriptions) {
                List<Route> subscriptionRoutes = routeService.findBy(subscription.getOrigin(), subscription.getDestination());

                subscriptionRoutes = subscriptionRoutes
                        .parallelStream()
                        .sorted(WEIGHTED_SUM_MODEL)
                        .limit(3)
                        .collect(Collectors.toList());

                List<RouteNotification> subscriptionRouteNotifications = new ArrayList<>();
                for (Route route : subscriptionRoutes) {
                    RouteNotificationDto routeNotificationDto = new RouteNotificationDto();
                    routeNotificationDto.setRoute(route);
                    routeNotificationDto.setSubscription(subscription);
                    RouteNotification saved = routeNotificationService.save(routeNotificationDto);
                    subscriptionRouteNotifications.add(saved);
                }
                subscriptionRouteNotifications.forEach(srn -> ownerRouteNotifications.put(subscription, srn));
            }
            log.info("Notifying owner {} about new cheap tickets", owner);
            emailService.sendNotifications(owner, ownerRouteNotifications.values());
        }
        return null;
    }
}
