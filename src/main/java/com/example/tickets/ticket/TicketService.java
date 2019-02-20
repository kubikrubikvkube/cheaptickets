package com.example.tickets.ticket;

import com.example.tickets.subscription.Subscription;
import com.google.common.collect.Multimap;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


public interface TicketService {
    Optional<Ticket> cheapest(String origin, String destination, LocalDate departureDate);
    Optional<Ticket> cheapest(String origin, String destination);


    List<Ticket> findBy(String origin, String destination);

    List<Ticket> findAll();

    List<Ticket> findAll(Sort sort);
    List<Ticket> findAll(int limit);

    List<Ticket> findAll(int limit, Sort sort);

    Multimap<String, String> findDistinctOriginAndDestination();

    List<Ticket> saveAll(List<TicketDto> tickets);

    List<Ticket> saveAllIfNotExist(List<TicketDto> tickets);

    List<Ticket> saveAllIfNotExist(List<TicketDto> tickets, boolean isParallel);

    long count();

    boolean exist(TicketDto ticket);

    List<Ticket> findBySubscription(Subscription subscription);

    Ticket save(TicketDto ticketDto);
}
