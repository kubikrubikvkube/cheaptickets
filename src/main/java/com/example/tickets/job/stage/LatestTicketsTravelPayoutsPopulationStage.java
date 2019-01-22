package com.example.tickets.job.stage;

import com.example.tickets.subscription.SubscriptionService;
import com.example.tickets.ticket.Ticket;
import com.example.tickets.ticket.TicketRepository;
import com.example.tickets.travelpayouts.TravelPayoutsService;
import com.example.tickets.travelpayouts.request.LatestRequest;
import com.google.common.base.Stopwatch;
import com.google.common.collect.Multimap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Component
public class LatestTicketsTravelPayoutsPopulationStage extends AbstractStage {
    private final Logger log = LoggerFactory.getLogger(LatestTicketsTravelPayoutsPopulationStage.class);
    private final TicketRepository ticketRepository;
    private final TravelPayoutsService travelPayoutsService;
    private final SubscriptionService subscriptionService;
    List<Ticket> ticketsFromTravelPayoutsService = new ArrayList<>();

    public LatestTicketsTravelPayoutsPopulationStage(TicketRepository ticketRepository, TravelPayoutsService travelPayoutsService, SubscriptionService subscriptionService) {
        this.ticketRepository = ticketRepository;
        this.travelPayoutsService = travelPayoutsService;
        this.subscriptionService = subscriptionService;
    }


//        subscriptions.forEach(subscription -> {
//            //TODO одну и ту же подписку от разных юзеров он будет молоть херову тучу раз, исправить
//
//
//            List<Ticket> latest = travelPayoutsService.getLatest(latestRequest);
//            ticketsFromTravelPayoutsService.addAll(latest);
//        });
//        var unsavedTicketsSize = ticketsFromTravelPayoutsService.size();
//        log.info(format("Found %d latest tickets from TravelPayout", unsavedTicketsSize));
//
//        ticketsFromTravelPayoutsService
//                .parallelStream()
//                .filter(ticket -> ticket.getNumberOfChanges() < 2)
//                .filter(ticket -> !ticketRepository.existsByOriginAndDestinationAndDepartDateAndValue(ticket.getOrigin(), ticket.getDestination(), ticket.getDepartDate(), ticket.getValue()))
//                .forEach(ticketRepository::save);
//        var endTime = Instant.now().toEpochMilli();
//        log.info("LatestTicketsTravelPayoutsPopulationStage finished in {}", timer.stop());


    @Override
    public StageResult call() throws InterruptedException {
        Stopwatch timer = Stopwatch.createStarted();

        log.info("LatestTicketsTravelPayoutsPopulationStage started");
        Multimap<String, String> subscriptionsMultimap = subscriptionService.findDistinctOriginAndDestination();
        int subscriptionsSize = subscriptionsMultimap.size();
        log.info("Found {} subscriptions", subscriptionsSize);
        Map<String, Collection<String>> subscriptionsMap = subscriptionsMultimap.asMap();
        List<LatestRequest> subscriptionRequests = new ArrayList<>(subscriptionsSize);
        for (Map.Entry<String, Collection<String>> entry : subscriptionsMap.entrySet()) {
            String origin = entry.getKey();
            Collection<String> destinations = entry.getValue();
            for (String destination : destinations) {
                LatestRequest latestRequest = LatestRequest.builder()
                        .origin(origin)
                        .destination(destination)
                        .limit(1000)
                        .one_way(true)
                        .build();
                subscriptionRequests.add(latestRequest);
            }
        }

        List<Ticket> foundTickets = subscriptionRequests
                .parallelStream()
                .map(travelPayoutsService::getLatest)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        AtomicInteger savedObjects = new AtomicInteger();

        foundTickets
                .parallelStream()
                .forEach(foundTicket -> {
                    Optional<? extends Ticket> alreadyStoredOptional = ticketRepository.findOne(Example.of(foundTicket));
                    if (alreadyStoredOptional.isEmpty()) {
                        savedObjects.incrementAndGet();
                        ticketRepository.saveAndFlush(foundTicket);
                    }
                });


        log.info("LatestTicketsTravelPayoutsPopulationStage finished in {}", timer.stop());
        return new StageResult("LatestTicketsTravelPayoutsPopulationStage", savedObjects.get(), 0, 0);
    }


}
