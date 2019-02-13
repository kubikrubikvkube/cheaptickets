package com.example.tickets.stages.stage;

import com.example.tickets.aviasales.AviasalesService;
import com.example.tickets.subscription.SubscriptionService;
import com.example.tickets.ticket.Ticket;
import com.example.tickets.ticket.TicketService;
import com.google.common.base.Stopwatch;
import com.google.common.collect.Multimap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

@Component
public class OnewayTicketsForAYearAviasalesStage implements Stage {
    private final static Logger log = LoggerFactory.getLogger(OnewayTicketsForAYearAviasalesStage.class);
    private final TicketService ticketService;
    private final SubscriptionService subscriptionService;
    private final AviasalesService aviasalesService;

    public OnewayTicketsForAYearAviasalesStage(TicketService ticketService, SubscriptionService subscriptionService, AviasalesService aviasalesService) {
        this.ticketService = ticketService;
        this.subscriptionService = subscriptionService;
        this.aviasalesService = aviasalesService;
    }

    @Override
    public StageResult call() {
        Stopwatch timer = Stopwatch.createStarted();
        log.info("OnewayTicketsForAYearAviasalesStage started");

        Multimap<String, String> subscriptionsMultimap = subscriptionService.findDistinctOriginAndDestination();
        int subscriptionsSize = subscriptionsMultimap.size();
        log.info("Found {} subscriptions", subscriptionsSize);
        Map<String, Collection<String>> subscriptionsMap = subscriptionsMultimap.asMap();

        log.info("Ticket retrieving started. It may take a while");
        AtomicLong foundTicketsCount = new AtomicLong();
        AtomicLong savedTicketsCount = new AtomicLong();
        for (Map.Entry<String, Collection<String>> entry : subscriptionsMap.entrySet()) {
            log.info("Processing subscriptions {}", entry);
            List<Ticket> foundTickets = new CopyOnWriteArrayList<>();
            String origin = entry.getKey();
            Collection<String> destinations = entry.getValue();
            Stream<LocalDate> dateStream = LocalDate.now().datesUntil(LocalDate.now().plusMonths(12));
            dateStream
                    .parallel()
                    .forEach(date -> {
                        destinations
                                .forEach(destination -> {
                                    log.debug("Processing request {} {} {}", origin, destination, date);
                                    List<Ticket> destinationTickets = aviasalesService.getOneWayTicket(origin, destination, date, 1);
                                    foundTickets.addAll(destinationTickets);
                                    List<Ticket> returnTickets = aviasalesService.getOneWayTicket(destination, origin, date, 1);
                                    foundTickets.addAll(returnTickets);
                                });
                    });
            //TODO считать depart ticket и return ticket отдельно
            foundTicketsCount.addAndGet(foundTickets.size());
            long savedTickets = ticketService.saveAllIfNotExist(foundTickets, true);
            savedTicketsCount.addAndGet(savedTickets);
            log.info("For subscriptions {} found {} ticket and saved {} tickets", entry, foundTickets.size(), savedTickets);
        }
        log.info("Overall found {} tickets and saved {} tickets", foundTicketsCount.get(), savedTicketsCount.get());
        log.info("OnewayTicketsForAYearAviasalesStage finished in {}", timer.stop());
        return new StageResult("OnewayTicketsForAYearAviasalesStage", savedTicketsCount.get(), 0, 0);
    }
}
