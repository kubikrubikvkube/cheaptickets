package com.example.tickets.statistics;

import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Month;
import java.util.Optional;

@Service
@Transactional
public class TicketStatisticsServiceImpl implements TicketStatisticsService {
    private final TicketStatisticsRepository repository;

    public TicketStatisticsServiceImpl(TicketStatisticsRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<TicketStatistics> findByOriginAndDestination(String origin, String destination) {
        return repository.findByOriginAndDestination(origin, destination);
    }

    @Override
    public Optional<TicketStatisticsByMonth> findByOriginAndDestination(String origin, String destination, Month month) {
        Optional<TicketStatistics> byOriginAndDestination = findByOriginAndDestination(origin, destination);
        if (byOriginAndDestination.isPresent()) {
            TicketStatistics statistics = byOriginAndDestination.get();
            return statistics.getTicketStatisticsByMonth()
                    .stream()
                    .filter(ticketStatisticsByMonth -> ticketStatisticsByMonth.getMonth().equals(month))
                    .findAny();
        }
        return Optional.empty();
    }

    @Override
    public long count() {
        return repository.count();
    }
}
