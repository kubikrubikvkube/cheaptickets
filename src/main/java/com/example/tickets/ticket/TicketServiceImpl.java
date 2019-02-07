package com.example.tickets.ticket;

import com.example.tickets.subscription.Subscription;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Comparator.comparing;

@Service
@Transactional
public class TicketServiceImpl implements TicketService {
    private final Logger log = LoggerFactory.getLogger(TicketServiceImpl.class);
    private final TicketRepository repository;
    private final ObjectMapper mapper;
    private final ExampleMatcher exampleMatcher;
    private final TicketDtoMapper dtoMapper;

    public TicketServiceImpl(TicketRepository repository, ObjectMapper mapper, TicketDtoMapper dtoMapper) {
        this.repository = repository;
        this.mapper = mapper;
        this.dtoMapper = dtoMapper;
        exampleMatcher = ExampleMatcher.matchingAll().withIgnorePaths("id", "creationTimestamp", "foundAt").withIncludeNullValues();
    }

    @Override
    public Optional<Ticket> cheapest(String origin, String destination, LocalDate departureDate) {
        List<Ticket> foundTickets = repository.findByOriginAndDestinationAndDepartDate(origin, destination, departureDate);

        return foundTickets
                .stream()
                .filter(ticket -> ticket.getValue() != null)
                .min(comparing(Ticket::getValue));
    }

    @Override
    public Optional<ObjectNode> prices(String origin, String destination, LocalDate departureDate) {
        List<Ticket> tickets = repository.findByOriginAndDestinationAndDepartDate(origin, destination, departureDate);
        ObjectNode node = mapper.createObjectNode();
        tickets.forEach(ticket -> node.put(ticket.getDepartDate().toString(), ticket.getValue()));
        if (!node.isEmpty(null)) {
            return Optional.of(node);
        }
        return Optional.empty();
    }

    @Override
    public Optional<ObjectNode> prices(String origin, String destination) {
        List<Ticket> tickets = repository.findByOriginAndDestination(origin, destination);
        ObjectNode node = mapper.createObjectNode();
        tickets.forEach(ticket -> node.put(ticket.getDepartDate().toString(), ticket.getValue()));
        if (!node.isEmpty(null)) {
            return Optional.of(node);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Ticket> cheapest(String origin, String destination) {
        List<Ticket> foundTickets = repository.findByOriginAndDestination(origin, destination);

        return foundTickets
                .stream()
                .filter(ticket -> ticket.getValue() != null)
                .min(comparing(Ticket::getValue));
    }

    @Override
    public List<Ticket> findBy(String origin, String destination) {
        return repository.findByOriginAndDestination(origin, destination);
    }

    @Override
    public List<Ticket> findAll() {
        return Lists.newArrayList(repository.findAll());
    }

    @Override
    public List<Ticket> findAll(int limit) {
        List<Ticket> allTickets = this.findAll();

        return allTickets
                .stream()
                .limit(limit)
                .collect(Collectors.toList());
    }

    @Override
    public void saveAll(List<Ticket> tickets) {
        repository.saveAll(tickets);
    }

    @Override
    public long saveAllIfNotExist(List<Ticket> tickets) {
        return saveAllIfNotExist(tickets, false);
    }

    @Override
    public long saveAllIfNotExist(List<Ticket> tickets, boolean isParallel) {
        Stream<Ticket> ticketStream;
        if (isParallel) {
            ticketStream = tickets.parallelStream();
        } else {
            ticketStream = tickets.stream();
        }
        AtomicLong counter = new AtomicLong();
        ticketStream
                .forEach(foundTicket -> {
                    log.debug("Processing {}", foundTicket);
                    boolean alreadyStored = this.exist(foundTicket);
                    log.debug("Is ticket already stored {}", alreadyStored);
                    if (!alreadyStored) {
                        log.debug("Not stored. Saving {}", foundTicket);
                        this.save(foundTicket);
                        counter.addAndGet(1);
                    }
                });
        return counter.get();
    }

    @Override
    public long count() {
        return repository.count();
    }


    @Override
    public boolean exist(Ticket ticket) {
        Example<Ticket> probe = Example.of(ticket, exampleMatcher);
        return repository.exists(probe);
    }

    @Override
    public List<Ticket> findBySubscription(Subscription subscription) {
        return repository.findBySubscription(subscription);
    }

    @Override
    public Ticket save(TicketDto ticketDto) {
        Ticket ticket = dtoMapper.fromDto(ticketDto);
        return save(ticket);
    }

    @Override
    public Ticket save(Ticket foundTicket) {
        return repository.save(foundTicket);
    }

}
