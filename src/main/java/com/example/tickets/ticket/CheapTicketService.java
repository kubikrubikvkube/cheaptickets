package com.example.tickets.ticket;

import com.example.tickets.subscription.Subscription;
import com.example.tickets.subscription.SubscriptionDto;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CheapTicketService {
    void save(CheapTicket cheapTicket);

    void save(Iterable<CheapTicket> cheapTickets);

    void save(Iterable<CheapTicket> cheapTickets, boolean isParallel);

    void saveIfNotExist(Iterable<CheapTicket> cheapTickets, boolean isParallel);

    void saveIfNotExist(Iterable<CheapTicket> cheapTickets);

    List<CheapTicket> findAll(Sort sort);

    List<CheapTicket> findAll();

    long count();

    void deleteAll();

    List<CheapTicket> findExpiredTickets(LocalDate departDate, boolean markedAsExpired);

    List<CheapTicket> findTicketsWithUnknownExpirationStatus();

    Optional<CheapTicket> findById(Long id);

    List<CheapTicket> findBySubscription(SubscriptionDto subscriptionDto);

    List<CheapTicket> findBySubscription(Subscription subscription);

    List<CheapTicket> findByOriginAndDestination(String origin, String destination);

    List<CheapTicket> findByOriginAndDestinationAndDepartDate(String origin, String destination, LocalDate departDate);

}
