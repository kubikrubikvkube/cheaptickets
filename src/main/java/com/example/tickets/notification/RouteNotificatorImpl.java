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

@Component
public class RouteNotificatorImpl implements RouteNotificator {
    private static final Logger log = LoggerFactory.getLogger(RouteNotificatorImpl.class);
    private final OwnerService ownerService;
    private final RouteNotificationService routeNotificationService;
    private final SubscriptionService subscriptionService;
    private final JavaMailSender javaMailSender;
    private final EmailService emailService;
    private final RouteNotificationDtoMapper notificationDtoMapper;

    public RouteNotificatorImpl(OwnerService ownerService, RouteNotificationService routeNotificationService, SubscriptionService subscriptionService, JavaMailSender javaMailSender, EmailService emailService, RouteNotificationDtoMapper notificationDtoMapper) {
        this.ownerService = ownerService;
        this.routeNotificationService = routeNotificationService;
        this.subscriptionService = subscriptionService;
        this.javaMailSender = javaMailSender;
        this.emailService = emailService;
        this.notificationDtoMapper = notificationDtoMapper;
    }

    @Override
    public RouteNotification notify(Subscription subscription, Route route) {
        RouteNotificationDto routeNotificationDto = new RouteNotificationDto();
        routeNotificationDto.setRoute(route);
        RouteNotification saved = routeNotificationService.save(routeNotificationDto);
        emailService.sendNotifications(subscription.getOwner(), List.of(saved));
        log.info("Owner {} notified about route {}", subscription, route);
        return saved;
    }

}
