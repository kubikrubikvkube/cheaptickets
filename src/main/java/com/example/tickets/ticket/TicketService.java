package com.example.tickets.ticket;

import com.fasterxml.jackson.databind.node.ObjectNode;

import java.time.LocalDate;


public interface TicketService {
    Ticket cheapest(String origin, String destination, LocalDate departureDate);

    ObjectNode prices(String origin, String destination, LocalDate departureDate);

    ObjectNode prices(String origin, String destination);
}
