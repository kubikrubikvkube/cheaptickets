package com.example.tickets.route;

import com.example.tickets.ticket.Ticket;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RouteDto {

    private String origin;
    private String destination;
    private Ticket departTicket;
    private Ticket returnTicket;
    private Integer sumValue;
    private Integer tripDurationInDays;
    private String affilateLink;

    public RouteDto(String origin, String destination) {
        this.origin = origin;
        this.destination = destination;
    }
}
