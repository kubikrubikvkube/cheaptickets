package com.example.tickets.notification;

import com.example.tickets.owner.OwnerService;
import com.example.tickets.route.Route;
import com.example.tickets.subscription.Subscription;
import com.example.tickets.subscription.SubscriptionService;
import com.example.tickets.util.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class RouteNotificatorImpl implements RouteNotificator {
    private final OwnerService ownerService;
    private final RouteNotificationService routeNotificationService;
    private final SubscriptionService subscriptionService;
    private final JavaMailSender javaMailSender;
    private final EmailService emailService;
    private final RouteNotificationDTOMapper notificationDTOMapper;
    private final Logger log = LoggerFactory.getLogger(RouteNotificatorImpl.class);

    public RouteNotificatorImpl(OwnerService ownerService, RouteNotificationService routeNotificationService, SubscriptionService subscriptionService, JavaMailSender javaMailSender, EmailService emailService, RouteNotificationDTOMapper notificationDTOMapper) {
        this.ownerService = ownerService;
        this.routeNotificationService = routeNotificationService;
        this.subscriptionService = subscriptionService;
        this.javaMailSender = javaMailSender;
        this.emailService = emailService;
        this.notificationDTOMapper = notificationDTOMapper;
    }

    @Override
    public Optional<RouteNotification> notify(Subscription subscription, Route route) {
        RouteNotificationDTO routeNotificationDTO = new RouteNotificationDTO();
        routeNotificationDTO.setSubscription(subscription);
        routeNotificationDTO.setRoute(route);
        emailService.sendNotification(subscription.getOwner(), route);
        log.info("Owner {} notified about route {}", subscription, route);
        RouteNotification saved = routeNotificationService.save(routeNotificationDTO);
        return Optional.ofNullable(saved);
    }

    @Override
    public List<RouteNotification> notify(Subscription subscription, List<Route> routes) {
        return null;
    }
}
