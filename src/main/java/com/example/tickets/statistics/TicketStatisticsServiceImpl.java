package com.example.tickets.statistics;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TicketStatisticsServiceImpl implements TicketStatisticsService {
    private final TicketStatisticsRepository repository;

    public TicketStatisticsServiceImpl(TicketStatisticsRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<TicketStatistics> findByOriginAndDestination(String origin, String destination) {
        return repository.findByOriginAndDestination(origin, destination);
    }
}
