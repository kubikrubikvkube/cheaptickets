package com.example.tickets.job;

import com.example.tickets.ticket.Ticket;
import com.example.tickets.ticket.TicketRepository;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class TicketInvalidationJob implements Job {
    private final Logger log = LoggerFactory.getLogger(TicketInvalidationJob.class);
    private final TicketRepository ticketRepository;

    public TicketInvalidationJob(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        List<Ticket> ticketsWithUnknownExpirationStatus = ticketRepository.findTicketsWithUnknownExpirationStatus();
        log.info(String.format("Found %d tickets with unknown expiration status", ticketsWithUnknownExpirationStatus.size()));
        ticketsWithUnknownExpirationStatus.parallelStream().forEach(ticket -> {
            var expiresAt = ticket.getExpiresAt();
            var departDate = ticket.getDepartDate();
            if ((expiresAt != null && expiresAt.isBefore(LocalDateTime.now())) || (departDate != null && departDate.isBefore(LocalDate.now()))) {
                ticket.setIsExpired(true);
            } else {
                ticket.setIsExpired(false);
            }
            ticketRepository.save(ticket);
        });
        //TODO пусть отдельная джоба занимается unknown expiration status'ами
        List<Ticket> expiredTickets = ticketRepository.findExpiredTickets(LocalDate.now(), false);
        log.info(String.format("Found %d actually expired, but not marked 'asExpired'", expiredTickets.size()));
        expiredTickets.forEach(ticket -> {
            ticket.setIsExpired(true);
            ticketRepository.save(ticket);
        });
    }
}
