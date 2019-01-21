package com.example.tickets.ticket;

import com.example.tickets.subscription.Subscription;
import com.example.tickets.subscription.SubscriptionDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Service
@Transactional
public class CheapTicketServiceImpl implements CheapTicketService {
    private final CheapTicketRepository repository;
    private final ObjectMapper objectMapper;

    public CheapTicketServiceImpl(CheapTicketRepository repository, ObjectMapper objectMapper) {
        this.repository = repository;
        this.objectMapper = objectMapper;
    }

    @Override
    public void save(CheapTicket cheapTicket) {
        repository.save(cheapTicket);
    }

    @Override
    public void saveAll(Iterable<CheapTicket> cheapTickets) {
        repository.saveAll(cheapTickets);
    }

    @Override
    public long count() {
        return repository.count();
    }

    @Override
    public void deleteAll() {
        repository.deleteAll();
    }

    @Override
    public List<CheapTicket> findExpiredTickets(LocalDate departDate, boolean markedAsExpired) {
        return repository.findExpiredTickets(departDate, markedAsExpired);
    }

    @Override
    public List<CheapTicket> findTicketsWithUnknownExpirationStatus() {
        return repository.findTicketsWithUnknownExpirationStatus();
    }

    @Override
    public List<CheapTicket> findBySubscription(SubscriptionDTO subscriptionDTO) {
        if (subscriptionDTO == null) {
            return Collections.emptyList();
        }
        return repository.findBySubscription(subscriptionDTO);
    }

    @Override
    public List<CheapTicket> findBySubscription(Subscription subscription) {
        if (subscription == null) {
            return Collections.emptyList();
        }
        return repository.findBySubscription(subscription);
    }

    @Override
    public List<CheapTicket> findByOriginAndDestination(String origin, String destination) {
        if (origin == null || destination == null) {
            return Collections.emptyList();
        }
        return repository.findByOriginAndDestination(origin, destination);
    }

    @Override
    public List<CheapTicket> findByOriginAndDestinationAndDepartDate(String origin, String destination, LocalDate departDate) {
        return repository.findByOriginAndDestinationAndDepartDate(origin, destination, departDate);
    }
}
