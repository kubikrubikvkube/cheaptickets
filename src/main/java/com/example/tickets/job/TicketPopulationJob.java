package com.example.tickets.job;

import com.example.tickets.aviasales.AviasalesService;
import com.example.tickets.subscription.SubscriptionRepository;
import com.example.tickets.ticket.Ticket;
import com.example.tickets.ticket.TicketRepository;
import com.example.tickets.travelpayouts.TravelPayoutsService;
import com.example.tickets.travelpayouts.request.LatestRequest;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class TicketPopulationJob implements Job {
    private final Logger logger = LoggerFactory.getLogger(TicketPopulationJob.class);
    private final TicketRepository ticketRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final TravelPayoutsService travelPayoutsService;
    private final AviasalesService aviasalesService;

    public TicketPopulationJob(TicketRepository ticketRepository, SubscriptionRepository subscriptionRepository, TravelPayoutsService travelPayoutsService, AviasalesService aviasalesService) {
        this.ticketRepository = ticketRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.travelPayoutsService = travelPayoutsService;
        this.aviasalesService = aviasalesService;
    }


    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        Map<String, String> directions = new HashMap<>();
        subscriptionRepository
                .findNonExpired()
                .forEach(s -> directions.put(s.getOrigin(), s.getDestination()));

        List<Ticket> nonSavedTickets = new ArrayList<>();
        directions.forEach((origin, destination) -> {
            LatestRequest latestRequest = LatestRequest.builder()
                    .origin(origin)
                    .destination(destination)
                    .one_way(true)
                    .build();

            List<Ticket> latest = travelPayoutsService.getLatest(latestRequest);
            nonSavedTickets.addAll(latest);
        });

        nonSavedTickets
                .parallelStream()
                .filter(ticket -> !ticketRepository.existsByOriginAndDestinationAndDepartDateAndValue(ticket.getOrigin(), ticket.getDestination(), ticket.getDepartDate(), ticket.getValue()))
                .forEach(ticketRepository::save);

    }


}
