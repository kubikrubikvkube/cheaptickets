package com.example.tickets.job.stage;

import com.example.tickets.notification.RouteNotification;
import com.example.tickets.notification.RouteNotificationDTO;
import com.example.tickets.notification.RouteNotificationDTOMapper;
import com.example.tickets.notification.RouteNotificationService;
import com.example.tickets.owner.Owner;
import com.example.tickets.owner.OwnerService;
import com.example.tickets.route.Route;
import com.example.tickets.route.RouteService;
import com.example.tickets.subscription.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class RouteNotificationStage implements Stage {
    private final OwnerService ownerService;
    private final RouteService routeService;
    private final RouteNotificationDTOMapper routeDTOMapper;
    private final RouteNotificationService routeNotificationService;
    private final Logger log = LoggerFactory.getLogger(RouteNotificationStage.class);

    public RouteNotificationStage(OwnerService ownerService, RouteService routeService, RouteNotificationDTOMapper routeDTOMapper, RouteNotificationService routeNotificationService) {
        this.ownerService = ownerService;
        this.routeService = routeService;
        this.routeDTOMapper = routeDTOMapper;
        this.routeNotificationService = routeNotificationService;
    }

    @Override
    public StageResult call() {
        List<Owner> allOwners = ownerService.findAll();
        Map<Subscription, List<RouteNotification>> ownerRouteNotifications = new HashMap<>();
        for (Owner owner : allOwners) {
            List<Subscription> ownerSubscriptions = owner.getSubscriptions();
            for (Subscription subscription : ownerSubscriptions) {
                List<Route> subscriptionRoutes = routeService.findBy(subscription.getOrigin(), subscription.getDestination(), 3);
                List<RouteNotification> subscriptionRouteNotifications = new ArrayList<>();
                for (Route route : subscriptionRoutes) {
                    RouteNotificationDTO routeNotificationDTO = new RouteNotificationDTO();
                    routeNotificationDTO.setRoute(route);
                    routeNotificationDTO.setSubscription(subscription);
                    RouteNotification saved = routeNotificationService.save(routeNotificationDTO);
                    subscriptionRouteNotifications.add(saved);
                }
                ownerRouteNotifications.put(subscription, subscriptionRouteNotifications);
            }
            log.info("Notifying owner {} about new cheap tickets");
            ownerRouteNotifications.forEach((subscription, routeNotifications) -> routeNotifications.forEach(routeNotification -> log.info(routeNotification.toString())));
        }

        return null;
    }
}
