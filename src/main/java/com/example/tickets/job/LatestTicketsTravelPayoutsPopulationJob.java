package com.example.tickets.job;

import com.example.tickets.aviasales.AviasalesService;
import com.example.tickets.subscription.Subscription;
import com.example.tickets.subscription.SubscriptionRepository;
import com.example.tickets.ticket.Ticket;
import com.example.tickets.ticket.TicketRepository;
import com.example.tickets.travelpayouts.TravelPayoutsService;
import com.example.tickets.travelpayouts.request.LatestRequest;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;

@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class LatestTicketsTravelPayoutsPopulationJob implements Job {
    private final Logger logger = LoggerFactory.getLogger(LatestTicketsTravelPayoutsPopulationJob.class);
    private final TicketRepository ticketRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final TravelPayoutsService travelPayoutsService;
    private final AviasalesService aviasalesService;

    public LatestTicketsTravelPayoutsPopulationJob(TicketRepository ticketRepository, SubscriptionRepository subscriptionRepository, TravelPayoutsService travelPayoutsService, AviasalesService aviasalesService) {
        this.ticketRepository = ticketRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.travelPayoutsService = travelPayoutsService;
        this.aviasalesService = aviasalesService;
    }


    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        logger.info("Starting LatestTicketsTravelPayoutsPopulationJob at " + LocalDateTime.now());
        List<Subscription> nonExpired = subscriptionRepository.findNonExpired();
        logger.info(format("Found %d non-expired subscriptions", nonExpired.size()));

        List<Ticket> travelPayoutsTickets = new ArrayList<>();
        nonExpired.forEach(subscription -> {
            //TODO одну и ту же подписку от разных юзеров он будет молоть херову тучу раз, исправить
            LatestRequest latestRequest = LatestRequest.builder()
                    .origin(subscription.getOrigin())
                    .destination(subscription.getDestination())
                    .limit(1000)
                    .one_way(true)
                    .build();

            List<Ticket> latest = travelPayoutsService.getLatest(latestRequest);
            travelPayoutsTickets.addAll(latest);
        });
        var unsavedTicketsSize = travelPayoutsTickets.size();
        logger.info(format("Found %d latest tickets from TravelPayout", unsavedTicketsSize));

        travelPayoutsTickets
                .parallelStream()
                .filter(ticket -> !ticketRepository.existsByOriginAndDestinationAndDepartDateAndValue(ticket.getOrigin(), ticket.getDestination(), ticket.getDepartDate(), ticket.getValue()))
                .forEach(ticketRepository::save);
        logger.info("LatestTicketsTravelPayoutsPopulationJob finished");



    }


}
