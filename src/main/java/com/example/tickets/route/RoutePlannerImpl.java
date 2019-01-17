package com.example.tickets.route;

import com.example.tickets.statistics.TicketStatistics;
import com.example.tickets.statistics.TicketStatisticsRepository;
import com.example.tickets.subscription.Subscription;
import com.example.tickets.ticket.Ticket;
import com.example.tickets.ticket.TicketRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class RoutePlannerImpl implements RoutePlanner {
    private final TicketRepository ticketRepository;
    private final TicketStatisticsRepository statisticsRepository;

    public RoutePlannerImpl(TicketRepository ticketRepository, TicketStatisticsRepository statisticsRepository) {
        this.ticketRepository = ticketRepository;
        this.statisticsRepository = statisticsRepository;
    }

    @Override
    public Route plan(Subscription subscription) {
        String origin = subscription.getOrigin();
        String destination = subscription.getDestination();
        Integer tripDurationInDays = subscription.getTripDurationInDays();
        List<Ticket> ticketList = ticketRepository.findByOriginAndDestination(origin, destination);
        Optional<TicketStatistics> statisticsOpt = statisticsRepository.findByOriginAndDestination(origin, destination);
        TicketStatistics ticketStatistics = statisticsOpt.orElseThrow();

//TODO всего по подписке 1046 вернулось 500 не работает
        return null;
    }
}
