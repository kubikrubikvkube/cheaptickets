package com.example.tickets.job.stage;

import com.example.tickets.owner.Owner;
import com.example.tickets.owner.OwnerService;
import com.example.tickets.route.Route;
import com.example.tickets.route.RouteService;
import com.example.tickets.subscription.Subscription;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class RouteNotificationStage implements Stage {
    private final OwnerService ownerService;
    private final RouteService routeService;

    public RouteNotificationStage(OwnerService ownerService, RouteService routeService) {
        this.ownerService = ownerService;
        this.routeService = routeService;
    }

    @Override
    public StageResult call() {
        List<Owner> allOwners = ownerService.findAll();
        Map<Subscription, List<Route>> ownerRoutes = new HashMap<>();
        for (Owner owner : allOwners) {
            List<Subscription> ownerSubscriptions = owner.getSubscriptions();
            for (Subscription subscription : ownerSubscriptions) {
                List<Route> subscriptionRoutes = routeService.findBy(subscription.getOrigin(), subscription.getDestination(), 3);
                ownerRoutes.put(subscription, subscriptionRoutes);
            }
        }
        return null;
    }
}
