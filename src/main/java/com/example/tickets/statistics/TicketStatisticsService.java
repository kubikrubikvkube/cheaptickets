package com.example.tickets.statistics;

import java.util.Optional;


public interface TicketStatisticsService {

    Optional<TicketStatistics> findByOriginAndDestination(String origin, String destination);
}
