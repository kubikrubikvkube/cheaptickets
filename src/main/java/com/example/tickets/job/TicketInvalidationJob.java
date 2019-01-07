package com.example.tickets.job;

import com.example.tickets.ticket.Ticket;
import com.example.tickets.ticket.TicketRepository;
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
public class TicketInvalidationJob implements Job {
    private final Logger log = LoggerFactory.getLogger(TicketInvalidationJob.class);
    private final TicketRepository ticketRepository;

    public TicketInvalidationJob(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    @Override
    public void execute(JobExecutionContext context) {
        var startTime = Instant.now().toEpochMilli();
        log.info("TicketInvalidationJob started");
        List<Ticket> expiredTickets = ticketRepository.findExpiredTickets(LocalDate.now(), false);
        log.info(String.format("Found %d actually expired, but not marked 'asExpired'", expiredTickets.size()));
        expiredTickets.forEach(ticket -> {
            ticket.setIsExpired(true);
            ticketRepository.save(ticket);
        });
        var endTime = Instant.now().toEpochMilli();
        log.info(format("TicketInvalidationJob finished in %d ms", endTime - startTime));
    }
}
