package com.example.tickets.job.stage;

import com.example.tickets.ticket.Ticket;
import com.example.tickets.ticket.TicketRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

import static java.lang.String.format;

@Component
public class TicketInvalidationStage extends AbstractStage {
    private final Logger log = LoggerFactory.getLogger(TicketInvalidationStage.class);

    private final TicketRepository ticketRepository;

    public TicketInvalidationStage(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }


    @Override
    public StageResult call() {
        var startTime = Instant.now().toEpochMilli();
        log.info("TicketInvalidationStage started");

        List<Ticket> expiredTickets = ticketRepository.findTicketsInPast(LocalDate.now(), false);

        long expiredTicketsCount = expiredTickets.size();

        log.info("Found {} actually expired tickets, but not marked 'asExpired'", expiredTicketsCount);

        expiredTickets.forEach(t -> t.setIsExpired(true));
        ticketRepository.saveAll(expiredTickets);
        ticketRepository.flush();

        var endTime = Instant.now().toEpochMilli();
        log.info(format("TicketInvalidationStage finished in %d ms", endTime - startTime));

        return new StageResult("TicketInvalidationStage", 0, expiredTicketsCount, 0);
    }

}
