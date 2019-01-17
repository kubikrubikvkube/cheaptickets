package com.example.tickets.route;

import com.example.tickets.notification.TicketNotification;
import com.example.tickets.ticket.Ticket;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class RouteDTO {

    public RouteDTO(String origin, String destination) {
        this.origin = origin;
        this.destination = destination;
    }

    private String origin;

    private String destination;

    private Ticket departTicket;

    private Ticket returnTicket;

    private Integer sumValue;

    private Integer tripDurationInDays;

    private List<TicketNotification> ticketNotifications;
}