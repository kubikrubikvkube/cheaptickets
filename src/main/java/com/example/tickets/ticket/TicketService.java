package com.example.tickets.ticket;

import com.fasterxml.jackson.databind.node.ObjectNode;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


public interface TicketService {
    Optional<Ticket> cheapest(String origin, String destination, LocalDate departureDate);

    Optional<ObjectNode> prices(String origin, String destination, LocalDate departureDate);

    Optional<ObjectNode> prices(String origin, String destination);

    Optional<Ticket> cheapest(String origin, String destination);

    List<Ticket> findBy(String origin, String destination);

    void saveAll(List<Ticket> tickets);
}
