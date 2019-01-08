package com.example.tickets.job;

import com.example.tickets.subscription.Subscription;
import com.example.tickets.subscription.SubscriptionRepository;
import com.example.tickets.ticket.TicketRepository;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class SubscriptionStatisticsUpdaterJob implements Job {
    private final Logger log = LoggerFactory.getLogger(SubscriptionStatisticsUpdaterJob.class);
    private final TicketRepository ticketRepository;
    private final SubscriptionRepository subscriptionRepository;

    public SubscriptionStatisticsUpdaterJob(TicketRepository ticketRepository, SubscriptionRepository subscriptionRepository) {
        this.ticketRepository = ticketRepository;
        this.subscriptionRepository = subscriptionRepository;
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        List<Subscription> subscriptions = subscriptionRepository.findNonExpired();
        //TODO как создавать статистику для подписки?
    }
}
