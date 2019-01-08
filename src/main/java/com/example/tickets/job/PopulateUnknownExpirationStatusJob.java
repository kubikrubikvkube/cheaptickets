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
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Consumer;

import static java.lang.String.format;

@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class PopulateUnknownExpirationStatusJob implements Job {
    private final Logger log = LoggerFactory.getLogger(PopulateUnknownExpirationStatusJob.class);
    private final TicketRepository ticketRepository;

    public PopulateUnknownExpirationStatusJob(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    private final Consumer<? super Ticket> setCorrectExpirationStatus = new Consumer<>() {
        @Override
        public void accept(Ticket ticket) {
            var dateNow = LocalDate.now();
            var dateTimeNow = LocalDateTime.now();
            var expiresAt = ticket.getExpiresAt();
            var departDate = ticket.getDepartDate();
            if ((expiresAt != null && expiresAt.isBefore(dateTimeNow)) || (departDate != null && departDate.isBefore(dateNow))) {
                ticket.setIsExpired(true);
            } else {
                ticket.setIsExpired(false);
            }
            ticketRepository.save(ticket);
        }
    };

    @Override
    public void execute(JobExecutionContext context) {
        var startTime = Instant.now().toEpochMilli();
        log.info("PopulateUnknownExpirationStatusJob started");
        List<Ticket> ticketsWithUnknownExpirationStatus = ticketRepository.findTicketsWithUnknownExpirationStatus();
        var size = ticketsWithUnknownExpirationStatus.size();
        log.info(format("Found %d tickets with unknown expiration status", size));

        if (size > 1000) {
            log.trace("Using parallel stream");
            ticketsWithUnknownExpirationStatus
                    .parallelStream()
                    .forEach(setCorrectExpirationStatus);
        } else {
            log.trace("Using default stream");
            ticketsWithUnknownExpirationStatus
                    .forEach(setCorrectExpirationStatus);
        }

        var endTime = Instant.now().toEpochMilli();
        log.info(format("PopulateUnknownExpirationStatusJob finished in %d ms", endTime - startTime));

    }

}
