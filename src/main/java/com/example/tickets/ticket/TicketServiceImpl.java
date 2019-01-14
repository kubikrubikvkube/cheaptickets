package com.example.tickets.ticket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static java.util.Comparator.comparing;

@Service
public class TicketServiceImpl implements TicketService {
    private final TicketRepository repository;
    private final ObjectMapper mapper;

    public TicketServiceImpl(TicketRepository repository, ObjectMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
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
}
