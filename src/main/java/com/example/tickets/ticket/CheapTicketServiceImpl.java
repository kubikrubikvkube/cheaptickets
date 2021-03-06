package com.example.tickets.ticket;

import com.example.tickets.subscription.Subscription;
import com.example.tickets.subscription.SubscriptionDto;
import lombok.NonNull;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Service
@Transactional
public class CheapTicketServiceImpl implements CheapTicketService {
    private final CheapTicketRepository repository;
    private final ExampleMatcher exampleMatcher;

    public CheapTicketServiceImpl(CheapTicketRepository repository) {
        this.repository = repository;
        this.exampleMatcher = ExampleMatcher.matchingAll().withIgnorePaths("id", "creationTimestamp", "foundAt").withIncludeNullValues();

    }

    @Override
    public void save(CheapTicket cheapTicket) {
        repository.save(cheapTicket);
    }

    @Override
    public void save(Iterable<CheapTicket> cheapTickets) {
        save(cheapTickets, false);
    }

    @Override
    public void save(Iterable<CheapTicket> cheapTickets, boolean isParallel) {

        repository.flush();
    }

    @Override
    public void saveIfNotExist(Iterable<CheapTicket> cheapTickets, boolean isParallel) {
        StreamSupport
                .stream(cheapTickets.spliterator(), isParallel)
                .forEach(ticket -> {
                    var ticketExample = Example.of(ticket, exampleMatcher);
                    if (!repository.exists(ticketExample)) {
                        repository.save(ticket);
                    }
                });
    }

    @Override
    public void saveIfNotExist(Iterable<CheapTicket> cheapTickets) {
        saveIfNotExist(cheapTickets, false);
    }

    @Override
    public List<CheapTicket> findAll() {
        return repository.findAll();
    }

    @Override
    public List<CheapTicket> findAll(Sort sort) {
        return repository.findAll(sort);
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
    public Optional<CheapTicket> findById(@NonNull Long id) {
        return repository.findById(id);
    }

    @Override
    public List<CheapTicket> findBySubscription(SubscriptionDto subscriptionDto) {
        if (subscriptionDto == null) {
            return Collections.emptyList();
        }
        return repository.findBySubscription(subscriptionDto);
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
