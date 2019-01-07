package com.example.tickets.job;

import com.example.tickets.aviasales.AviasalesService;
import com.example.tickets.subscription.Subscription;
import com.example.tickets.subscription.SubscriptionRepository;
import com.example.tickets.ticket.Ticket;
import com.example.tickets.ticket.TicketRepository;
import com.example.tickets.travelpayouts.TravelPayoutsService;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.PersistJobDataAfterExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

import static java.lang.String.format;

@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class OnewayTicketsForAYearAviasalesJob implements Job {
    private final Logger log = LoggerFactory.getLogger(OnewayTicketsForAYearAviasalesJob.class);
    private final TicketRepository ticketRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final AviasalesService aviasalesService;

    public OnewayTicketsForAYearAviasalesJob(TicketRepository ticketRepository, SubscriptionRepository subscriptionRepository, TravelPayoutsService travelPayoutsService, AviasalesService aviasalesService) {
        this.ticketRepository = ticketRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.aviasalesService = aviasalesService;
    }

    @Override
    public void execute(JobExecutionContext context) {
        //TODO code duplication
        var startTime = Instant.now().toEpochMilli();
        log.info("OnewayTicketsForAYearAviasalesJob started");
        List<Subscription> nonExpired = subscriptionRepository.findNonExpired();
        log.info(format("Found %d non-expired subscriptions", nonExpired.size()));

        nonExpired
                .forEach(subscription -> LocalDate.now().datesUntil(LocalDate.now().plusMonths(12))
                        .parallel()
                        .forEach(date -> {
                            List<Ticket> latest = aviasalesService.getOneWayTicket(subscription.getOrigin(), subscription.getDestination(), date, 1);
                            latest.stream()
                                    .filter(ticket -> !ticketRepository.existsByOriginAndDestinationAndDepartDateAndValue(ticket.getOrigin(), ticket.getDestination(), ticket.getDepartDate(), ticket.getValue()))
                                    .forEach(ticketRepository::save);
                        }));

        var endTime = Instant.now().toEpochMilli();
        log.info(format("OnewayTicketsForAYearAviasalesJob finished in %d ms", endTime - startTime));
    }
}
