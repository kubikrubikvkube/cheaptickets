package com.example.tickets.notification;

import com.example.tickets.route.Route;
import com.example.tickets.subscription.Subscription;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RouteNotificationDTO {
    Route route;
    private Subscription subscription;
}
