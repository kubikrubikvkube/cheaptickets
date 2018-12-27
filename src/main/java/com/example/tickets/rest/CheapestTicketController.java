package com.example.tickets.rest;

import com.example.tickets.repository.Ticket;
import com.example.tickets.repository.TicketRepository;
import com.example.tickets.service.TravelPayoutsService;
import com.example.tickets.service.aviasales.AviasalesService;
import com.example.tickets.util.DateConverter;
import lombok.extern.java.Log;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@RestController
@Log
public class CheapestTicketController {
    @Autowired
    TravelPayoutsService travelPayoutsService;
    @Autowired
    TicketRepository ticketRepository;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    AviasalesService aviasalesService;

    @RequestMapping("/cheapest")
    //TODO пусть берёт только из базы
    public List<Ticket> cheapest(@RequestParam(value = "origin") String origin,
                                 @RequestParam(value = "destination") String destination,
                                 @RequestParam(value = "departureDate") String departureDate) {

        LocalDate parsedLocalDate = LocalDate.parse(departureDate);
        Date date = DateConverter.toDate(parsedLocalDate);
        //
        List<Ticket> oneWayTickets = aviasalesService.getOneWayTicket(origin, destination, parsedLocalDate, 1);
        log.info(String.format("Found %d tickets", oneWayTickets.size()));
        ticketRepository.saveAll(oneWayTickets);
        //
        List<Ticket> found = ticketRepository.findByOriginAndDestinationAndDepartDate(origin, destination, date);
        ticketRepository.deleteAll(oneWayTickets);
        return found;

    }
}
