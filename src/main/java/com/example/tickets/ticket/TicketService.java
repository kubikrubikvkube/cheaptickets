package com.example.tickets.ticket;

import com.example.tickets.subscription.Subscription;
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

    List<Ticket> findAll();

    List<Ticket> findAll(int limit);

    void saveAll(List<Ticket> tickets);

    long saveAllIfNotExist(List<Ticket> tickets);

    long saveAllIfNotExist(List<Ticket> tickets, boolean isParallel);

    long count();

    boolean exist(Ticket ticket);

    List<Ticket> findBySubscription(Subscription subscription);

    Ticket save(TicketDTO ticketDTO);

    Ticket save(Ticket foundTicket);
}
