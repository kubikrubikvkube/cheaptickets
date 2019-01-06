package com.example.tickets.ticket;

import com.example.tickets.aviasales.AviasalesService;
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
public class CheapestTicketController {
    private final Logger log = LoggerFactory.getLogger(CheapestTicketController.class);
    private final TicketRepository ticketRepository;
    private final ModelMapper modelMapper;
    private final AviasalesService aviasalesService;

    public CheapestTicketController(TicketRepository ticketRepository, ModelMapper modelMapper, AviasalesService aviasalesService) {
        this.ticketRepository = ticketRepository;
        this.modelMapper = modelMapper;
        this.aviasalesService = aviasalesService;
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
}
