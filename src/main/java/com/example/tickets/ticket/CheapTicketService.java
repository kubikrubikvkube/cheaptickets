package com.example.tickets.ticket;

import com.example.tickets.subscription.Subscription;
import com.example.tickets.subscription.SubscriptionDTO;

import java.time.LocalDate;
import java.util.List;

public interface CheapTicketService {
    void save(CheapTicket cheapTicket);

    void save(Iterable<CheapTicket> cheapTickets);

    void save(Iterable<CheapTicket> cheapTickets, boolean isParallel);

    void saveIfNotExist(Iterable<CheapTicket> cheapTickets, boolean isParallel);

    void saveIfNotExist(Iterable<CheapTicket> cheapTickets);

    long count();

    void deleteAll();

    List<CheapTicket> findExpiredTickets(LocalDate departDate, boolean markedAsExpired);

    List<CheapTicket> findTicketsWithUnknownExpirationStatus();

    List<CheapTicket> findBySubscription(SubscriptionDTO subscriptionDTO);

    List<CheapTicket> findBySubscription(Subscription subscription);

    List<CheapTicket> findByOriginAndDestination(String origin, String destination);

    List<CheapTicket> findByOriginAndDestinationAndDepartDate(String origin, String destination, LocalDate departDate);

}
