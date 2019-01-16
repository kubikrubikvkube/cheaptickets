package com.example.tickets.job;

import com.example.tickets.statistics.*;
import com.example.tickets.subscription.Subscription;
import com.example.tickets.subscription.SubscriptionRepository;
import com.example.tickets.ticket.Ticket;
import com.example.tickets.ticket.TicketRepository;
import com.google.common.primitives.Doubles;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.time.LocalDate;
import java.time.Month;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
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
        List<TicketStatistics> statisticsList = new ArrayList<>();
        for (Subscription subscription : subscriptions) {
            var origin = subscription.getOrigin();
            var destination = subscription.getDestination();
            Optional<TicketStatistics> subscriptionStatistics = statisticsRepository.findByOriginAndDestination(origin, destination);
            TicketStatistics statistics = subscriptionStatistics.orElseGet(TicketStatistics::new);
            statistics.setOrigin(origin);
            statistics.setDestination(destination);
            statistics.setTicketStatisticsByDay(byDay(subscription));
            statistics.setTicketStatisticsByMonth(byMonth(subscription));
            statisticsList.add(statistics);
        }
        statisticsRepository.saveAll(statisticsList);
        var endTime = Instant.now().toEpochMilli();
        log.info(format("TicketStatisticsUpdaterJob finished in %d ms", endTime - startTime));
    }

    private List<TicketStatisticsByMonth> byMonth(Subscription s) {
        List<Ticket> subscriptionTickets = ticketRepository.findBySubscription(s);
        var earliestTicketForSubscriptionOpt = subscriptionTickets.stream().min(Comparator.comparing(Ticket::getDepartDate));
        var latestTicketOpt = subscriptionTickets.stream().max(Comparator.comparing(Ticket::getDepartDate));
        if (earliestTicketForSubscriptionOpt.isEmpty() || latestTicketOpt.isEmpty()) {
            return Collections.emptyList();
        }

        LocalDate earliestDepartDate = earliestTicketForSubscriptionOpt.get().getDepartDate();
        LocalDate latestDepartDate = latestTicketOpt.get().getDepartDate();

        List<TicketStatisticsByMonth> statisticsList = new ArrayList<>();
        long monthsRange = LocalDate.from(earliestDepartDate).until(latestDepartDate, ChronoUnit.MONTHS);

        for (int i = 0; i < monthsRange; i++) {
            int year = earliestDepartDate.plusMonths(i).getYear();
            Month month = earliestDepartDate.plusMonths(i).getMonth();
            DescriptiveStatistics ds = new DescriptiveStatistics();
            subscriptionTickets
                    .stream()
                    .filter(ticket -> ticket.getDepartDate().getMonth().equals(month))
                    .forEach(ticket -> ds.addValue(ticket.getValue()));

            TicketStatisticsByMonthDTO statDTO = new TicketStatisticsByMonthDTO();
            statDTO.setYear(year);
            statDTO.setMonth(month);
            statDTO.setTicketsCount((long) Doubles.asList(ds.getValues()).size());
            statDTO.setAvgTicketPrice(ds.getMean());
            statDTO.setMinTicketPrice(ds.getMin());
            statDTO.setPercentile10(ds.getPercentile(10));
            TicketStatisticsByMonth stat = TicketStatisticsByMonthDTOMapper.INSTANCE.fromDTO(statDTO);
            statisticsList.add(stat);
        }

        return statisticsList;
    }

    private List<TicketStatisticsByDay> byDay(Subscription s) {
        List<Ticket> subscriptionTickets = ticketRepository.findBySubscription(s);
        var earliestTicketForSubscriptionOpt = subscriptionTickets.stream().min(Comparator.comparing(Ticket::getDepartDate));
        var latestTicketOpt = subscriptionTickets.stream().max(Comparator.comparing(Ticket::getDepartDate));
        if (earliestTicketForSubscriptionOpt.isEmpty() || latestTicketOpt.isEmpty()) {
            return Collections.emptyList();
        }

        LocalDate earliestDepartDate = earliestTicketForSubscriptionOpt.get().getDepartDate();
        LocalDate latestDepartDate = latestTicketOpt.get().getDepartDate();

        List<TicketStatisticsByDay> byDayStatistics = new ArrayList<>();
        Stream<LocalDate> daysUntil = earliestDepartDate.datesUntil(latestDepartDate, Period.ofDays(1));

        daysUntil.forEach(day -> {
            List<Ticket> todayTickets = subscriptionTickets
                    .stream()
                    .filter(ticket -> ticket.getDepartDate().equals(day))
                    .collect(Collectors.toList());
            long count = todayTickets.size();

            todayTickets.forEach(ticket -> {
                DescriptiveStatistics ds = new DescriptiveStatistics();
                ds.addValue(ticket.getValue());
                TicketStatisticsByDayDTO statDTO = new TicketStatisticsByDayDTO();
                statDTO.setDate(day);
                statDTO.setTicketsCount(count);
                statDTO.setAvgTicketPrice(ds.getMean());
                statDTO.setMinTicketPrice(ds.getMin());
                statDTO.setPercentile10(ds.getPercentile(10));
                TicketStatisticsByDay stat = TicketStatisticsByDayDTOMapper.INSTANCE.dtoToTicketStatisticsByDay(statDTO);
                byDayStatistics.add(stat);
            });
        });

        return byDayStatistics;
    }
}
