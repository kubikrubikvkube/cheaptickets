package com.example.tickets.stages.stage;

import com.example.tickets.statistics.*;
import com.example.tickets.subscription.Subscription;
import com.example.tickets.subscription.SubscriptionService;
import com.example.tickets.ticket.Ticket;
import com.example.tickets.ticket.TicketService;
import com.google.common.base.Stopwatch;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.Month;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class TicketStatisticsUpdaterStage implements Stage {
    private static final Logger log = LoggerFactory.getLogger(TicketStatisticsUpdaterStage.class);
    private final TicketStatisticsService ticketStatisticsService;
    private final TicketService ticketService;
    private final SubscriptionService subscriptionService;
    private final TicketStatisticsByMonthDtoMapper ticketStatisticsByMonthDtoMapper;


    public TicketStatisticsUpdaterStage(TicketStatisticsService ticketStatisticsService, TicketService ticketService, SubscriptionService subscriptionService, TicketStatisticsByMonthDtoMapper ticketStatisticsByMonthDtoMapper) {
        this.ticketStatisticsService = ticketStatisticsService;
        this.ticketService = ticketService;
        this.subscriptionService = subscriptionService;
        this.ticketStatisticsByMonthDtoMapper = ticketStatisticsByMonthDtoMapper;

    }


    @Override
    public StageResult call() {
        Stopwatch timer = Stopwatch.createStarted();
        log.info("TicketStatisticsUpdaterStage started");
        ticketStatisticsService.deleteAll();//TODO опять не работает апдейт. Но работает добавление в БД если она пустая
        AtomicLong updatedCounter = new AtomicLong();

        Multimap<String, String> distinctOriginAndDestination = ticketService.findDistinctOriginAndDestination();

        List<Subscription> allSubscriptions = subscriptionService.findAll();
        log.info("Found {} subscriptions", allSubscriptions.size());
        log.info("Starting subscription processing");
        distinctOriginAndDestination.forEach((origin, destination) -> {
            log.debug("Processing {} {}", origin, destination);
            Optional<TicketStatistics> subscriptionStatisticsOpt = ticketStatisticsService.findByOriginAndDestination(origin, destination);
            TicketStatistics statistics = subscriptionStatisticsOpt.orElseGet(TicketStatistics::new);
            statistics.setOrigin(origin);
            statistics.setDestination(destination);
            statistics.setTicketStatisticsByMonth(byMonth(origin, destination));
            log.debug("Ticket statistics generated {}", statistics);
            TicketStatistics savedTicketStatistics = ticketStatisticsService.save(statistics);
            log.debug("Ticket statistics saved {}", savedTicketStatistics);
            updatedCounter.incrementAndGet();
        });


        log.info("TicketStatisticsUpdaterStage finished in {}", timer.stop());
        return new StageResult("TicketStatisticsUpdaterStage", 0, updatedCounter.get(), 0);
    }

    private List<TicketStatisticsByMonth> byMonth(String origin, String destination) {
        List<Ticket> subscriptionTickets = ticketService.findBy(origin, destination);
        subscriptionTickets.sort(Comparator.comparing(Ticket::getDepartDate));

        Multimap<Month, Ticket> ticketsAsMultimap = ArrayListMultimap.create();
        for (Ticket ticket : subscriptionTickets) {
            Month month = ticket.getDepartDate().getMonth();
            ticketsAsMultimap.put(month, ticket);
        }

        List<TicketStatisticsByMonthDto> statisticsListDto = new ArrayList<>();
        Map<Month, Collection<Ticket>> monthTicketCollection = ticketsAsMultimap.asMap();
        monthTicketCollection.forEach((Month month, Collection<Ticket> tickets) -> {
            DescriptiveStatistics ds = new DescriptiveStatistics();
            tickets.forEach(ticket -> {
                if (ticket.getNumberOfChanges() >= 2) {
                    log.debug("Ignoring ticket {} - too much changes", ticket);
                } else {
                    ds.addValue(ticket.getValue());
                }
            });

            TicketStatisticsByMonthDto stat = new TicketStatisticsByMonthDto();
            stat.setMonth(month);
            stat.setTicketsCount(tickets.size());
            stat.setAvgTicketPrice(ds.getMean());
            stat.setMinTicketPrice(ds.getMin());
            stat.setPercentile10(ds.getPercentile(10));
            statisticsListDto.add(stat);
        });
        List<TicketStatisticsByMonth> statisticsList = new ArrayList<>();
        statisticsListDto.forEach(dto -> statisticsList.add(ticketStatisticsByMonthDtoMapper.fromDto(dto)));

        return statisticsList;
    }


}
