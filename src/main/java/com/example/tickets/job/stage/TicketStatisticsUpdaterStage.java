package com.example.tickets.job.stage;

import com.example.tickets.statistics.TicketStatistics;
import com.example.tickets.statistics.TicketStatisticsByMonth;
import com.example.tickets.statistics.TicketStatisticsByMonthDTOMapper;
import com.example.tickets.statistics.TicketStatisticsService;
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
public class TicketStatisticsUpdaterStage extends AbstractStage {
    private final Logger log = LoggerFactory.getLogger(TicketStatisticsUpdaterStage.class);
    private final TicketStatisticsService ticketStatisticsService;
    private final TicketService ticketService;
    private final SubscriptionService subscriptionService;
    private final TicketStatisticsByMonthDTOMapper ticketStatisticsByMonthDTOMapper;


    public TicketStatisticsUpdaterStage(TicketStatisticsService ticketStatisticsService, TicketService ticketService, SubscriptionService subscriptionService, TicketStatisticsByMonthDTOMapper ticketStatisticsByMonthDTOMapper) {
        this.ticketStatisticsService = ticketStatisticsService;
        this.ticketService = ticketService;
        this.subscriptionService = subscriptionService;
        this.ticketStatisticsByMonthDTOMapper = ticketStatisticsByMonthDTOMapper;

    }


    @Override
    public StageResult call() {
        Stopwatch timer = Stopwatch.createStarted();
        log.info("TicketStatisticsUpdaterStage started");

        AtomicLong updatedCounter = new AtomicLong();
        List<Subscription> allSubscriptions = subscriptionService.findAll();
        for (Subscription subscription : allSubscriptions) {
            var origin = subscription.getOrigin();
            var destination = subscription.getDestination();
            Optional<TicketStatistics> subscriptionStatisticsOpt = ticketStatisticsService.findByOriginAndDestination(origin, destination);
            TicketStatistics statistics = subscriptionStatisticsOpt.orElseGet(TicketStatistics::new);
            statistics.setOrigin(origin);
            statistics.setDestination(destination);
            statistics.setTicketStatisticsByMonth(byMonth(subscription));
            Optional<TicketStatistics> update = ticketStatisticsService.update(statistics);
            if (update.isPresent()) {
                updatedCounter.incrementAndGet();
            }
        }

        log.info("TicketStatisticsUpdaterStage finished in {}", timer.stop());
        return new StageResult("TicketStatisticsUpdaterStage", 0, updatedCounter.get(), 0);
    }

    private List<TicketStatisticsByMonth> byMonth(Subscription s) {
        List<Ticket> subscriptionTickets = ticketService.findBySubscription(s);
        subscriptionTickets.sort(Comparator.comparing(Ticket::getDepartDate));

        Multimap<Month, Ticket> ticketsAsMultimap = ArrayListMultimap.create();
        for (Ticket ticket : subscriptionTickets) {
            Month month = ticket.getDepartDate().getMonth();
            ticketsAsMultimap.put(month, ticket);
        }

        List<TicketStatisticsByMonth> statisticsList = new ArrayList<>();
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

            TicketStatisticsByMonth stat = new TicketStatisticsByMonth();
            stat.setMonth(month);
            stat.setTicketsCount(tickets.size());
            stat.setAvgTicketPrice(ds.getMean());
            stat.setMinTicketPrice(ds.getMin());
            stat.setPercentile10(ds.getPercentile(10));
            statisticsList.add(stat);
        });

        return statisticsList;
    }
}
