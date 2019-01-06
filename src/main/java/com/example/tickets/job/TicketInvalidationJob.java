package com.example.tickets.job;

import com.example.tickets.ticket.Ticket;
import com.example.tickets.ticket.TicketRepository;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Random;

@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class TicketInvalidationJob implements Job {
    private final Logger log = LoggerFactory.getLogger(TicketInvalidationJob.class);
    @Autowired
    TicketRepository ticketRepository;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //
        var t = new Ticket();
        t.setOrigin("LED");
        t.setDestination("MOW");
        t.setDepartDate(LocalDate.now());
        t.setDepartTime(LocalTime.now().plusHours(12));
        t.setValue(new Random().nextInt());
        ticketRepository.save(t);
        //
        List<Ticket> expiredTickets = ticketRepository.findExpiredTickets(LocalDate.now(), LocalTime.now(), false);
        log.info(String.format("Found %d actually expired, but not marked 'asExpired'", expiredTickets.size()));
    }
}
