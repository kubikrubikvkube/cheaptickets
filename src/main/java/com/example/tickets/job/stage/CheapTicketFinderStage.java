package com.example.tickets.job.stage;

import com.example.tickets.statistics.TicketStatistics;
import com.example.tickets.statistics.TicketStatisticsByMonth;
import com.example.tickets.statistics.TicketStatisticsService;
import com.example.tickets.subscription.SubscriptionService;
import com.example.tickets.ticket.*;
import com.google.common.base.Stopwatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class CheapTicketFinderStage extends AbstractStage {
    private final Logger log = LoggerFactory.getLogger(CheapTicketFinderStage.class);
    private final TicketStatisticsService ticketStatisticsService;
    private final TicketService ticketService;
    private final SubscriptionService subscriptionService;
    private final CheapTicketMapper cheapTicketMapper;
    private final CheapTicketService cheapTicketService;

    public CheapTicketFinderStage(TicketStatisticsService ticketStatisticsService, TicketService ticketService, SubscriptionService subscriptionService, CheapTicketMapper cheapTicketMapper, CheapTicketService cheapTicketService) {
        this.ticketStatisticsService = ticketStatisticsService;
        this.ticketService = ticketService;
        this.subscriptionService = subscriptionService;
        this.cheapTicketMapper = cheapTicketMapper;
        this.cheapTicketService = cheapTicketService;
    }

    @Override
    public StageResult call() {
        //TODO in progress
        Stopwatch timer = Stopwatch.createStarted();
        log.info("CheapTicketFinderStage started");

        List<Ticket> allTickets = ticketService.findAll();
        log.info("Found {} tickets total", allTickets.size());

        List<CheapTicket> cheapestTickets = new CopyOnWriteArrayList<>();
        allTickets.parallelStream().forEach(ticket -> {
            var origin = ticket.getOrigin();
            var destination = ticket.getDestination();
            Optional<TicketStatistics> statisticsOpt = ticketStatisticsService.findByOriginAndDestination(origin, destination);
            if (statisticsOpt.isPresent()) {
                TicketStatistics statistics = statisticsOpt.get();
                var ticketDepartureMonth = ticket.getDepartDate().getMonth();
                Optional<TicketStatisticsByMonth> statisticsByMonthOpt = statistics.getTicketStatisticsByMonth()
                        .stream()
                        .filter(s -> s.getMonth().equals(ticketDepartureMonth))
                        .findFirst();

                if (statisticsByMonthOpt.isPresent()) {
                    TicketStatisticsByMonth statisticsByMonth = statisticsByMonthOpt.get();
                    if (ticket.getValue() <= statisticsByMonth.getPercentile10()) {
                        CheapTicket cheapTicket = cheapTicketMapper.toCheapTicket(ticket);
                        cheapestTickets.add(cheapTicket);
                    }
                } else {
                    log.debug("Ticket statistics is not found for month {}", ticketDepartureMonth);
                }
            }
        });

        log.info("Found {} cheapest tickets", cheapestTickets.size());
        cheapTicketService.saveAll(cheapestTickets, true);
        log.info("Saved {} cheapest tickets", cheapestTickets.size());
        log.info("CheapTicketFinderStage finished in {}", timer.stop());
        return null;
    }
}
