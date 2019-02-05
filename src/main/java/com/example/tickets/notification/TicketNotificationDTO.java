package com.example.tickets.notification;

import com.example.tickets.owner.Owner;
import com.example.tickets.route.Route;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TicketNotificationDTO {
    Route route;
    private Owner owner;
}
