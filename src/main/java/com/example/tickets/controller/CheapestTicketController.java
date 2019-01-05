package com.example.tickets.controller;

import com.example.tickets.aviasales.AviasalesService;
import com.example.tickets.ticket.Ticket;
import com.example.tickets.ticket.TicketRepository;
import lombok.extern.java.Log;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.bind.DatatypeConverter;
import java.util.Date;

@RestController
@Log
public class CheapestTicketController {
    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private AviasalesService aviasalesService;

    @RequestMapping("/cheapest")
    public Ticket cheapest(@RequestParam(value = "origin") String origin,
                           @RequestParam(value = "destination") String destination,
                           @RequestParam(value = "departureDate") String departureDate) {

        Date departureDateDate = DatatypeConverter.parseDateTime(departureDate).getTime();

        boolean isExists = ticketRepository.existsByOriginAndDestinationAndDepartDate(origin, destination, departureDateDate);
        if (isExists) {
            return ticketRepository.findCheapestForDate(origin, destination, departureDateDate);
        }
        return null;
    }
}
