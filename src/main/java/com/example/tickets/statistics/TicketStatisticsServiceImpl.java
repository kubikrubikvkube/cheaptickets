package com.example.tickets.statistics;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Month;
import java.util.Optional;

@Service
@Transactional
public class TicketStatisticsServiceImpl implements TicketStatisticsService {
    private final TicketStatisticsRepository repository;
    private final ExampleMatcher exampleMatcher;

    public TicketStatisticsServiceImpl(TicketStatisticsRepository repository) {
        this.repository = repository;
        this.exampleMatcher = ExampleMatcher.matchingAll().withIgnorePaths("id", "ticketStatisticsByMonth", "createdAt", "foundAt").withIncludeNullValues();
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
    public Optional<TicketStatistics> update(TicketStatistics statistics) {
        Example<TicketStatistics> example = Example.of(statistics, exampleMatcher);

        Optional<TicketStatistics> found = repository.findOne(example);
        if (found.isPresent()) {
            repository.deleteById(found.get().getId());
            repository.flush();
            repository.saveAndFlush(statistics);
        } else {
            repository.saveAndFlush(statistics);
        }

        return repository.findOne(example);
    }

    @Override
    public void deleteAll() {
        repository.deleteAll();
    }

    @Override
    public boolean exist(TicketStatistics statistics) {
        return repository.exists(Example.of(statistics, exampleMatcher));
    }

    @Override
    public long count() {
        return repository.count();
    }
}
