package com.example.tickets.job.stage;

import com.example.tickets.ticket.Ticket;
import com.example.tickets.ticket.TicketRepository;
import com.google.common.base.Stopwatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class TicketInvalidationStage implements Stage {
    private final Logger log = LoggerFactory.getLogger(TicketInvalidationStage.class);

    private final TicketRepository ticketRepository;

    public TicketInvalidationStage(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    @Override
    public StageResult call() {
        Stopwatch timer = Stopwatch.createStarted();
        log.info("TicketInvalidationStage started");

        List<Ticket> expiredTickets = ticketRepository.findTicketsInPast(LocalDate.now());

        long expiredTicketsCount = expiredTickets.size();

        log.info("Found {} actually expired tickets, but not deleted from database", expiredTicketsCount);

        if (!expiredTickets.isEmpty()) {
            ticketRepository.deleteAll(expiredTickets);
            ticketRepository.flush();
        }

        log.info("TicketInvalidationStage finished in {}", timer.stop());
        return new StageResult("TicketInvalidationStage", 0, 0, expiredTicketsCount);
    }

}
