package com.example.tickets.controller;

import com.example.tickets.aviasales.AviasalesService;
import com.example.tickets.ticket.Ticket;
import com.example.tickets.ticket.TicketRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.bind.DatatypeConverter;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

@RestController
public class CheapestTicketController {
    private final Logger log = LoggerFactory.getLogger(CheapestTicketController.class);
    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private AviasalesService aviasalesService;

    @RequestMapping("/cheapest")
    public Optional<Ticket> cheapest(@RequestParam(value = "origin") String origin,
                                     @RequestParam(value = "destination") String destination,
                                     @RequestParam(value = "departureDate") String departureDate) {

        OffsetDateTime departureDateTime = DatatypeConverter.parseDateTime(departureDate).toInstant().atOffset(ZoneOffset.UTC);
        return ticketRepository.findCheapestForDate(origin, destination, departureDateTime);

    }
}
