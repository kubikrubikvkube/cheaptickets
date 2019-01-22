package com.example.tickets.job;

import com.example.tickets.statistics.TicketStatistics;
import com.example.tickets.statistics.TicketStatisticsByMonth;
import com.example.tickets.statistics.TicketStatisticsService;
import com.example.tickets.subscription.SubscriptionService;
import com.example.tickets.ticket.*;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.lang.String.format;

@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class CheapTicketFinderJob implements Job {
    private final Logger log = LoggerFactory.getLogger(CheapTicketFinderJob.class);
    private final TicketStatisticsService ticketStatisticsService;
    private final TicketService ticketService;
    private final SubscriptionService subscriptionService;
    private final CheapTicketMapper cheapTicketMapper;
    private final CheapTicketService cheapTicketService;

    public CheapTicketFinderJob(TicketStatisticsService ticketStatisticsService, TicketService ticketService, SubscriptionService subscriptionService, CheapTicketMapper cheapTicketMapper, CheapTicketService cheapTicketService) {
        this.ticketStatisticsService = ticketStatisticsService;
        this.ticketService = ticketService;
        this.subscriptionService = subscriptionService;
        this.cheapTicketMapper = cheapTicketMapper;
        this.cheapTicketService = cheapTicketService;
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        var startTime = Instant.now().toEpochMilli();
        log.info("CheapTicketFinderJob started");

        List<Ticket> allTickets = ticketService.findAll();
        log.info("Found {} tickets total", allTickets.size());

        List<CheapTicket> cheapestTickets = new ArrayList<>();
        for (Ticket ticket : allTickets) {
            var origin = ticket.getOrigin();
            var destination = ticket.getDestination();
            Optional<TicketStatistics> statisticsOpt = ticketStatisticsService.findByOriginAndDestination(origin, destination);
            if (statisticsOpt.isEmpty()) return;
//           TODO     throw new ServiceException(String.format("Ticket statistics is not present for %s and %s", origin, destination));
            TicketStatistics statistics = statisticsOpt.get();
            var ticketDepartureMonth = ticket.getDepartDate().getMonth();
            Optional<TicketStatisticsByMonth> statisticsByMonthOpt = statistics.getTicketStatisticsByMonth().stream().filter(s -> s.getMonth().equals(ticketDepartureMonth)).findFirst();
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
        log.info("Found {} cheapest tickets", cheapestTickets.size());
        cheapTicketService.saveAll(cheapestTickets);
        log.info("Saved {} cheapest tickets", cheapestTickets.size());
        var endTime = Instant.now().toEpochMilli();
        log.info(format("CheapTicketFinderJob finished in %d ms", endTime - startTime));
    }
}
