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
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.exact;

@Component
public class LatestTicketsTravelPayoutsPopulationStage extends AbstractStage {
    private final Logger log = LoggerFactory.getLogger(LatestTicketsTravelPayoutsPopulationStage.class);
    private final TicketRepository ticketRepository;
    private final TravelPayoutsService travelPayoutsService;
    private final SubscriptionService subscriptionService;

    public LatestTicketsTravelPayoutsPopulationStage(TicketRepository ticketRepository, TravelPayoutsService travelPayoutsService, SubscriptionService subscriptionService) {
        this.ticketRepository = ticketRepository;
        this.travelPayoutsService = travelPayoutsService;
        this.subscriptionService = subscriptionService;
    }

    @Override
    public StageResult call() {
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
        log.info("Generated {} subscription requests", subscriptionRequests.size());
        List<Ticket> foundTickets = subscriptionRequests
                .parallelStream()
                .map(travelPayoutsService::getLatest)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        log.info("Subscription requests processed");
        log.info("Got {} tickets from TravelPayouts", foundTickets.size());

        log.info("Ticket saving started");
        AtomicInteger savedObjects = new AtomicInteger();

        ExampleMatcher ticketMatcher = ExampleMatcher.matchingAny()
                .withMatcher("origin", exact())
                .withMatcher("destination", exact())
                .withMatcher("value", exact())
                .withMatcher("departDate", exact())
                .withMatcher("foundAt", exact())
                .withMatcher("numberOfChanges", exact());

        foundTickets
                .parallelStream()
                .forEach(foundTicket -> {
                    log.debug("Processing {}", foundTicket);
                    //TODO работать через DTO??? Чтобы игнорить айдишники. И вообще выпилить юзание репозитория нахер из стейджа, а юзать сервис.
                    Optional<Ticket> alreadyStoredOptional = ticketRepository.findOne(Example.of(foundTicket, ticketMatcher));
                    log.debug("Already stored ticket optional {}", alreadyStoredOptional);
                    if (alreadyStoredOptional.isEmpty()) {
                        log.debug("Not stored. Saving {}", foundTicket);
                        ticketRepository.save(foundTicket);
                        savedObjects.incrementAndGet();
                    }
                });
        ticketRepository.flush();

        log.info("LatestTicketsTravelPayoutsPopulationStage finished in {}", timer.stop());
        return new StageResult("LatestTicketsTravelPayoutsPopulationStage", savedObjects.get(), 0, 0);
    }


}
