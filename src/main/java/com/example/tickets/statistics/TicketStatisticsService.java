package com.example.tickets.statistics;

import java.time.Month;
import java.util.Optional;


public interface TicketStatisticsService {

    Optional<TicketStatistics> findByOriginAndDestination(String origin, String destination);

    Optional<TicketStatisticsByMonth> findByOriginAndDestination(String origin, String destination, Month month);

    long count();
}
