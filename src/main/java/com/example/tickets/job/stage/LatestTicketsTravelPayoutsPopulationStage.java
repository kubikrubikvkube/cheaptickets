package com.example.tickets.job.stage;

import com.example.tickets.subscription.SubscriptionService;
import com.example.tickets.ticket.Ticket;
import com.example.tickets.ticket.TicketService;
import com.example.tickets.travelpayouts.TravelPayoutsService;
import com.example.tickets.travelpayouts.request.LatestRequest;
import com.google.common.base.Stopwatch;
import com.google.common.collect.Multimap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class LatestTicketsTravelPayoutsPopulationStage implements AbstractStage {
    private final Logger log = LoggerFactory.getLogger(LatestTicketsTravelPayoutsPopulationStage.class);
    private final TravelPayoutsService travelPayoutsService;
    private final SubscriptionService subscriptionService;
    private final TicketService ticketService;

    public LatestTicketsTravelPayoutsPopulationStage(TravelPayoutsService travelPayoutsService, SubscriptionService subscriptionService, TicketService ticketService) {
        this.travelPayoutsService = travelPayoutsService;
        this.subscriptionService = subscriptionService;
        this.ticketService = ticketService;

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
                log.debug("Processing subscription from {} to {}", origin, destination);
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
        long savedTickets = ticketService.saveAllIfNotExist(foundTickets, true);
        log.info("LatestTicketsTravelPayoutsPopulationStage finished in {}", timer.stop());
        return new StageResult("LatestTicketsTravelPayoutsPopulationStage", savedTickets, 0, 0);
    }


}
