package com.example.tickets.ticket;

import com.example.tickets.subscription.Subscription;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Comparator.comparing;

@Service
@Transactional
public class TicketServiceImpl implements TicketService {
    private static final Logger log = LoggerFactory.getLogger(TicketServiceImpl.class);
    private final TicketRepository repository;
    private final ObjectMapper mapper;
    private final ExampleMatcher exampleMatcher;
    private final TicketDtoMapper dtoMapper;

    public TicketServiceImpl(TicketRepository repository, ObjectMapper mapper, TicketDtoMapper dtoMapper) {
        this.repository = repository;
        this.mapper = mapper;
        this.dtoMapper = dtoMapper;
        this.exampleMatcher = ExampleMatcher.matchingAll().withIgnorePaths("id", "creationTimestamp", "foundAt").withIncludeNullValues();
    }

    @Override
    public Optional<Ticket> cheapest(String origin, String destination, LocalDate departureDate) {
        List<Ticket> foundTickets = repository.findByOriginAndDestinationAndDepartDate(origin, destination, departureDate);

        return foundTickets
                .stream()
                .min(comparing(Ticket::getValue));
    }

    @Override
    public Optional<Ticket> cheapest(String origin, String destination) {
        List<Ticket> foundTickets = repository.findByOriginAndDestination(origin, destination);

        return foundTickets
                .stream()
                .min(comparing(Ticket::getValue));
    }

    @Override
    public List<Ticket> findBy(String origin, String destination) {
        return repository.findByOriginAndDestination(origin, destination);
    }

    @Override
    public Multimap<String, String> findDistinctOriginAndDestination() {
        Multimap<String, String> map = ArrayListMultimap.create();
        repository.findAll().forEach(ticket -> {
            var origin = ticket.getOrigin();
            var destination = ticket.getDestination();
            if (!map.containsEntry(origin, destination)) {
                map.put(ticket.getOrigin(), ticket.getDestination());
            }
        });
        return map;
    }

    @Override
    public List<Ticket> saveAll(List<TicketDto> tickets) {
        List<Ticket> ticketPatterns = tickets
                .stream()
                .map(dtoMapper::fromDto)
                .collect(Collectors.toList());

        return repository.saveAll(ticketPatterns);
    }

    @Override
    public List<Ticket> findAll() {
        return repository.findAll();
    }

    @Override
    public List<Ticket> findAll(Sort sort) {
        return repository.findAll(sort);
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
    public List<Ticket> findAll(int limit, Sort sort) {
        List<Ticket> allTickets = this.findAll(sort);

        return allTickets
                .stream()
                .limit(limit)
                .collect(Collectors.toList());
    }

    @Override
    public List<Ticket> saveAllIfNotExist(List<TicketDto> tickets) {
        return saveAllIfNotExist(tickets, false);
    }

    @Override
    public List<Ticket> saveAllIfNotExist(List<TicketDto> tickets, boolean isParallel) {
        Stream<TicketDto> ticketStream;
        if (isParallel) {
            ticketStream = tickets.parallelStream();
        } else {
            ticketStream = tickets.stream();
        }
        AtomicLong counter = new AtomicLong();
        List<Ticket> savedTickets = new CopyOnWriteArrayList<>();
        ticketStream
                .forEach(ticketDto -> {
                    log.debug("Processing {}", ticketDto);
                    boolean alreadyStored = this.exist(ticketDto);
                    log.debug("Is ticketDto already stored {}", alreadyStored);
                    if (!alreadyStored) {
                        log.debug("Not stored. Saving {}", ticketDto);
                        Ticket saved = this.save(ticketDto);
                        savedTickets.add(saved);
                        counter.addAndGet(1);
                    }
                });
        return savedTickets;
    }

    @Override
    public long count() {
        return repository.count();
    }


    @Override
    public boolean exist(TicketDto ticketDto) {
        Example<Ticket> probe = Example.of(dtoMapper.fromDto(ticketDto), exampleMatcher);
        return repository.exists(probe);
    }

    @Override
    public List<Ticket> findBySubscription(Subscription subscription) {
        return repository.findBySubscription(subscription);
    }

    @Override
    public Ticket save(TicketDto ticketDto) {
        Ticket ticket = dtoMapper.fromDto(ticketDto);
        return repository.save(ticket);
    }


}
