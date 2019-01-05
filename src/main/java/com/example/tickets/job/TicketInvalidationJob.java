package com.example.tickets.job;

import com.example.tickets.ticket.Ticket;
import com.example.tickets.ticket.TicketRepository;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.OffsetDateTime;
import java.util.List;

@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class TicketInvalidationJob implements Job {
    private final Logger log = LoggerFactory.getLogger(TicketInvalidationJob.class);
    @Autowired
    TicketRepository ticketRepository;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        List<Ticket> expiredTickets = ticketRepository.findExpiredTickets(OffsetDateTime.now(), false);
        log.info(String.format("Found %d actually expired, but not marked 'asExpired'", expiredTickets.size()));
    }
}
