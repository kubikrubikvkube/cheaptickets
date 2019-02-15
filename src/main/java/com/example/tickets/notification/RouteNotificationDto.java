package com.example.tickets.notification;

import com.example.tickets.route.Route;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode
public class RouteNotificationDto {
    Route route;
}
