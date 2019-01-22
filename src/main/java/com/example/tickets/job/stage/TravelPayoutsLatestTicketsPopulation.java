package com.example.tickets.job.stage;

import com.example.tickets.subscription.Subscription;
import com.example.tickets.subscription.SubscriptionRepository;
import com.example.tickets.ticket.Ticket;
import com.example.tickets.ticket.TicketRepository;
import com.example.tickets.travelpayouts.TravelPayoutsService;
import com.example.tickets.travelpayouts.request.LatestRequest;
import com.google.common.base.Stopwatch;
import com.google.common.collect.Iterables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;

@Component
public class TravelPayoutsLatestTicketsPopulation extends AbstractStage {
    private final Logger log = LoggerFactory.getLogger(TravelPayoutsLatestTicketsPopulation.class);
    private final TicketRepository ticketRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final TravelPayoutsService travelPayoutsService;

    public TravelPayoutsLatestTicketsPopulation(TicketRepository ticketRepository, SubscriptionRepository subscriptionRepository, TravelPayoutsService travelPayoutsService) {
        this.ticketRepository = ticketRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.travelPayoutsService = travelPayoutsService;
    }

    @Override
    public StageResult call() {
        Stopwatch timer = Stopwatch.createStarted();
        log.info("LatestTicketsTravelPayoutsPopulationJob started");
        Iterable<Subscription> subscriptions = subscriptionRepository.findAll();
        log.info(format("Found %d subscriptions", Iterables.size(subscriptions)));

        List<Ticket> travelPayoutsTickets = new ArrayList<>();
        subscriptions.forEach(subscription -> {
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
        log.info(format("Found %d latest tickets from TravelPayout", unsavedTicketsSize));

        travelPayoutsTickets
                .parallelStream()
                .filter(ticket -> ticket.getNumberOfChanges() < 2)
                .filter(ticket -> !ticketRepository.existsByOriginAndDestinationAndDepartDateAndValue(ticket.getOrigin(), ticket.getDestination(), ticket.getDepartDate(), ticket.getValue()))
                .forEach(ticketRepository::save);
        var endTime = Instant.now().toEpochMilli();
        log.info("TravelPayoutsLatestTicketsPopulation finished in {}", timer.stop());
        return new StageResult("TravelPayoutsLatestTicketsPopulation", 0, 0, 0);
    }

}
