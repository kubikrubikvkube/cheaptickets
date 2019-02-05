package com.example.tickets.notification;

import com.example.tickets.owner.Owner;
import com.example.tickets.owner.OwnerService;
import com.example.tickets.route.Route;
import com.example.tickets.subscription.Subscription;
import com.example.tickets.subscription.SubscriptionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class RouteNotificatorImpl implements RouteNotificator {
    private final OwnerService ownerService;
    private final TicketNotificationService ticketNotificationService;
    private final SubscriptionService subscriptionService;
    private final JavaMailSender javaMailSender;
    private final TicketNotificationDTOMapper notificationDTOMapper;
    private final Logger log = LoggerFactory.getLogger(RouteNotificatorImpl.class);

    public RouteNotificatorImpl(OwnerService ownerService, TicketNotificationService ticketNotificationService, SubscriptionService subscriptionService, JavaMailSender javaMailSender, TicketNotificationDTOMapper notificationDTOMapper) {
        this.ownerService = ownerService;
        this.ticketNotificationService = ticketNotificationService;
        this.subscriptionService = subscriptionService;
        this.javaMailSender = javaMailSender;
        this.notificationDTOMapper = notificationDTOMapper;
    }

    @Override
    public Optional<TicketNotification> notify(Owner owner, Route route) {
        List<TicketNotificationDTO> notificationDTOS = owner.getSubscriptions()
                .stream()
                .map(Subscription::getNotifiedRoutes)
                .flatMap(Collection::stream)
                .map(notificationDTOMapper::toDTO)
                .collect(Collectors.toList());

        TicketNotificationDTO ticketNotificationDTO = new TicketNotificationDTO();
        ticketNotificationDTO.setOwner(owner);
        ticketNotificationDTO.setRoute(route);

        if (notificationDTOS.contains(ticketNotificationDTO)) {
            return Optional.empty();
        }

        //TicketNotificationService.вышлиПисьмо()
        log.info("Owner {} notified about route {}", owner, route);


        TicketNotification saved = ticketNotificationService.save(ticketNotificationDTO);

        return Optional.ofNullable(saved);
    }

    @Override
    public List<TicketNotification> notify(Owner owner, List<Route> routes) {
        return null;
    }
}
