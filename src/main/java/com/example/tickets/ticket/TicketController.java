package com.example.tickets.ticket;

import com.example.tickets.aviasales.AviasalesService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static java.util.Comparator.comparing;

@RestController
public class TicketController {
    private final Logger log = LoggerFactory.getLogger(TicketController.class);
    private final TicketRepository ticketRepository;
    private final ModelMapper modelMapper;
    private final AviasalesService aviasalesService;
    private final ObjectMapper objectMapper;

    public TicketController(TicketRepository ticketRepository, ModelMapper modelMapper, AviasalesService aviasalesService, ObjectMapper objectMapper) {
        this.ticketRepository = ticketRepository;
        this.modelMapper = modelMapper;
        this.aviasalesService = aviasalesService;
        this.objectMapper = objectMapper;
    }

    @RequestMapping("/ticket/cheapest")
    public Optional<Ticket> cheapest(@RequestParam(value = "origin") String origin,
                                     @RequestParam(value = "destination") String destination,
                                     @RequestParam(value = "departureDate") String departureDate) {


        List<Ticket> foundTickets = ticketRepository.findByOriginAndDestinationAndDepartDate(origin, destination, LocalDate.parse(departureDate));
        if (foundTickets.isEmpty()) {
            return Optional.empty();
        }

        return foundTickets
                .stream()
                .filter(ticket -> ticket.getValue() != null)
                .min(comparing(Ticket::getValue));
    }

    @RequestMapping(name = "/ticket/prices", params = {"origin", "destination", "departureDate"})
    public ObjectNode prices(@RequestParam(value = "origin") String origin,
                             @RequestParam(value = "destination") String destination,
                             @RequestParam(value = "departureDate") String departureDate) {


        List<Ticket> tickets = ticketRepository.findByOriginAndDestinationAndDepartDate(origin, destination, LocalDate.parse(departureDate));
        ObjectNode node = objectMapper.createObjectNode();
        tickets.forEach(ticket -> node.put(ticket.getDepartDate().toString(), ticket.getValue()));
        return node;
    }

    @RequestMapping(name = "/ticket/prices", params = {"origin", "destination"})
    public ObjectNode prices(@RequestParam(value = "origin") String origin,
                             @RequestParam(value = "destination") String destination) {


        List<Ticket> tickets = ticketRepository.findByOriginAndDestination(origin, destination);
        ObjectNode node = objectMapper.createObjectNode();
        tickets.forEach(ticket -> node.put(ticket.getDepartDate().toString(), ticket.getValue()));
        return node;
    }
}
