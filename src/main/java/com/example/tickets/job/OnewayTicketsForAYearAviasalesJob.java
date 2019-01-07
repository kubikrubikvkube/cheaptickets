package com.example.tickets.job;

import com.example.tickets.aviasales.AviasalesService;
import com.example.tickets.subscription.Subscription;
import com.example.tickets.subscription.SubscriptionRepository;
import com.example.tickets.ticket.Ticket;
import com.example.tickets.ticket.TicketRepository;
import com.example.tickets.travelpayouts.TravelPayoutsService;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static java.lang.String.format;

@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class OnewayTicketsForAYearAviasalesJob implements Job {
    private final Logger logger = LoggerFactory.getLogger(OnewayTicketsForAYearAviasalesJob.class);
    private final TicketRepository ticketRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final AviasalesService aviasalesService;

    public OnewayTicketsForAYearAviasalesJob(TicketRepository ticketRepository, SubscriptionRepository subscriptionRepository, TravelPayoutsService travelPayoutsService, AviasalesService aviasalesService) {
        this.ticketRepository = ticketRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.aviasalesService = aviasalesService;
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //TODO code duplication
        logger.info("Starting OnewayTicketsForAYearAviasalesJob at " + LocalDateTime.now());
        List<Subscription> nonExpired = subscriptionRepository.findNonExpired();
        logger.info(format("Found %d non-expired subscriptions", nonExpired.size()));

        nonExpired
                .forEach(subscription -> LocalDate.now().datesUntil(LocalDate.now().plusMonths(12))
                        .parallel()
                        .forEach(date -> {
                            List<Ticket> latest = aviasalesService.getOneWayTicket(subscription.getOrigin(), subscription.getDestination(), date, 1);
                            latest.stream()
                                    .filter(ticket -> !ticketRepository.existsByOriginAndDestinationAndDepartDateAndValue(ticket.getOrigin(), ticket.getDestination(), ticket.getDepartDate(), ticket.getValue()))
                                    .forEach(ticketRepository::save);
                        }));

        logger.info("OnewayTicketsForAYearAviasalesJob finished");
    }
}
