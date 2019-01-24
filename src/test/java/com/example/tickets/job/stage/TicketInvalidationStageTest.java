package com.example.tickets.job.stage;

import com.example.tickets.ticket.Ticket;
import com.example.tickets.ticket.TicketRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.util.Lists.emptyList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class TicketInvalidationStageTest {

    @Test
    void found_some_expired_tickets() {
        Ticket expiredTicket = new Ticket();
        List<Ticket> oneExpiredTicket = Collections.singletonList(expiredTicket);
        TicketRepository ticketRepository = mock(TicketRepository.class);
        when(ticketRepository.findTicketsInPast(LocalDate.now())).thenReturn(oneExpiredTicket);

        TicketInvalidationStage stage = new TicketInvalidationStage(ticketRepository);
        StageResult result = stage.call();
        assertEquals(1, result.getDeletedObjects());
    }

    @Test
    void found_none_expired_tickets() {
        TicketRepository ticketRepository = mock(TicketRepository.class);
        when(ticketRepository.findTicketsInPast(LocalDate.now())).thenReturn(emptyList());

        TicketInvalidationStage stage = new TicketInvalidationStage(ticketRepository);
        StageResult result = stage.call();
        assertEquals(0, result.getDeletedObjects());
    }
}