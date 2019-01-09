package com.example.tickets.statistics;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class TicketStatisticsController {
    private final TicketStatisticsRepository repository;

    public TicketStatisticsController(TicketStatisticsRepository repository) {
        this.repository = repository;
    }

    @RequestMapping(value = "/ticket/statistics", params = {"origin", "destination"})
    public Optional<TicketStatistics> get(@RequestParam String origin, @RequestParam String destination) {
        return repository.findByOriginAndDestination(origin, destination);
    }
}
