package com.example.tickets.job;

import com.example.tickets.statistics.TicketStatistics;
import com.example.tickets.statistics.TicketStatisticsByDay;
import com.example.tickets.statistics.TicketStatisticsByMonth;
import com.example.tickets.statistics.TicketStatisticsRepository;
import com.example.tickets.subscription.Subscription;
import com.example.tickets.subscription.SubscriptionRepository;
import com.example.tickets.ticket.Ticket;
import com.example.tickets.ticket.TicketRepository;
import com.google.common.primitives.Doubles;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math3.util.IntegerSequence;
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
        Iterable<Subscription> subscriptions = subscriptionRepository.findAll();
        for (Subscription subscription : subscriptions) {
            var origin = subscription.getOrigin();
            var destination = subscription.getDestination();
            Optional<TicketStatistics> subscriptionStatistics = statisticsRepository.findByOriginAndDestination(origin, destination);
            TicketStatistics statistics = subscriptionStatistics.orElseGet(TicketStatistics::new);
            statistics.setOrigin(origin);
            statistics.setDestination(destination);
            statistics.setTicketStatisticsByDay(byDay(subscription));
            statistics.setTicketStatisticsByMonth(byMonth(subscription));
            statisticsRepository.save(statistics);
        }
        var endTime = Instant.now().toEpochMilli();
        log.info(format("TicketStatisticsUpdaterJob finished in %d ms", endTime - startTime));
    }

    private List<TicketStatisticsByMonth> byMonth(Subscription s) {
        //TODO не работает если статистика идёт до следующего января следующего года так как и там и там первый месяц
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
        int earliestMonth = earliestDepartDate.getMonth().getValue();
        int latestMonth = latestDepartDate.getMonth().getValue();
        List<Ticket> subscriptionTickets = ticketRepository.findBySubscription(s);
        List<TicketStatisticsByMonth> statisticsList = new ArrayList<>();
        IntegerSequence.range(earliestMonth, latestMonth).forEach(month -> {
            DescriptiveStatistics ds = new DescriptiveStatistics();
            subscriptionTickets
                    .stream()
                    .filter(ticket -> ticket.getDepartDate().getMonth().getValue() == month)
                    .forEach(ticket -> ds.addValue(ticket.getValue()));
            TicketStatisticsByMonth statistics = new TicketStatisticsByMonth();
            statistics.setTicketsCount((long) Doubles.asList(ds.getValues()).size());
            statistics.setAvgTicketPrice(ds.getMean());
            statistics.setMinTicketPrice(ds.getMin());
            statistics.setPercentile5(ds.getPercentile(5));
            statisticsList.add(statistics);
        });
        return statisticsList;
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
