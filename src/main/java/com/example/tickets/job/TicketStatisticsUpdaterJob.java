package com.example.tickets.job;

import com.example.tickets.statistics.TicketStatistics;
import com.example.tickets.statistics.TicketStatisticsByDay;
import com.example.tickets.statistics.TicketStatisticsRepository;
import com.example.tickets.subscription.Subscription;
import com.example.tickets.subscription.SubscriptionRepository;
import com.example.tickets.ticket.Ticket;
import com.example.tickets.ticket.TicketRepository;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.util.*;
import java.util.stream.Stream;

import static java.lang.String.format;

@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class TicketStatisticsUpdaterJob implements Job {
    private final Logger log = LoggerFactory.getLogger(TicketStatisticsUpdaterJob.class);
    private final TicketStatisticsRepository statisticsRepository;
    private final TicketRepository ticketRepository;
    private final SubscriptionRepository subscriptionRepository;

    public TicketStatisticsUpdaterJob(TicketStatisticsRepository statisticsRepository, TicketRepository ticketRepository, SubscriptionRepository subscriptionRepository) {
        this.statisticsRepository = statisticsRepository;
        this.ticketRepository = ticketRepository;
        this.subscriptionRepository = subscriptionRepository;
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        var startTime = Instant.now().toEpochMilli();
        log.info("TicketStatisticsUpdaterJob started");
        List<Subscription> subscriptions = subscriptionRepository.findNonExpired();
        for (Subscription subscription : subscriptions) {
            var origin = subscription.getOrigin();
            var destination = subscription.getDestination();
            Optional<TicketStatistics> subscriptionStatistics = statisticsRepository.findByOriginAndDestination(origin, destination);
            TicketStatistics statistics = subscriptionStatistics.orElseGet(TicketStatistics::new);
            statistics.setOrigin(origin);
            statistics.setDestination(destination);
            statistics.setTicketStatisticsByDay(byDay(subscription));
            statisticsRepository.save(statistics);
        }
        var endTime = Instant.now().toEpochMilli();
        log.info(format("TicketStatisticsUpdaterJob finished in %d ms", endTime - startTime));
    }

    private List<TicketStatisticsByDay> byDay(Subscription s) {
        var origin = s.getOrigin();
        var destination = s.getDestination();
        var earliestTicketForSubscriptionOpt = ticketRepository.findBySubscription(s)
                .stream()
                .min(Comparator.comparing(Ticket::getDepartDate));
        var latestTicketOpt = ticketRepository.findBySubscription(s)
                .stream()
                .max(Comparator.comparing(Ticket::getDepartDate));

        if (earliestTicketForSubscriptionOpt.isEmpty() || latestTicketOpt.isEmpty()) return Collections.emptyList();
        LocalDate earliestDepartDate = earliestTicketForSubscriptionOpt.get().getDepartDate();
        LocalDate latestDepartDate = latestTicketOpt.get().getDepartDate();
        Stream<LocalDate> observableDays = earliestDepartDate.datesUntil(latestDepartDate, Period.ofDays(1));
        List<TicketStatisticsByDay> byDayStatistics = new ArrayList<>();
        observableDays.forEach(date -> {
            TicketStatisticsByDay day = new TicketStatisticsByDay();
            day.setDate(date);
            day.setTicketsCount(ticketRepository.countByOriginAndDestinationAndDepartDate(origin, destination, date));
            day.setAvgTicketPrice(statisticsRepository.calculateAvgTicketPriceForDate(origin, destination, date));
            day.setMinTicketPrice(statisticsRepository.calculateMinTicketPriceForDate(origin, destination, date));
            day.setPercentile5(statisticsRepository.calculate5PercentileTicketPriceForDate(origin, destination, date));
            byDayStatistics.add(day);
        });
        return byDayStatistics;
    }
}
