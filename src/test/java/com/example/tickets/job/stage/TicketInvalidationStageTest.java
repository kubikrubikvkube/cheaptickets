package com.example.tickets.job.stage;

import com.example.tickets.ticket.Ticket;
import com.example.tickets.ticket.TicketRepository;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class TicketInvalidationStageTest {


    @Test
    void found_some_expired_tickets() {
        Ticket expiredTicket = new Ticket();
        List<Ticket> oneExpiredTicket = Collections.singletonList(expiredTicket);
        TicketRepository ticketRepository = mock(TicketRepository.class);
        when(ticketRepository.findTicketsInPast(LocalDate.now())).thenReturn(oneExpiredTicket);
        doNothing().when(ticketRepository).deleteAll(oneExpiredTicket);
        doNothing().when(ticketRepository).flush();

        TicketInvalidationStage stage = new TicketInvalidationStage(ticketRepository);
        StageResult result = stage.call();
        assertEquals(1, result.getDeletedObjects());
        verify(ticketRepository, times(1)).findTicketsInPast(LocalDate.now());
        verify(ticketRepository, times(1)).deleteAll(oneExpiredTicket);
        verify(ticketRepository, times(1)).flush();
    }

    @Test
    void found_none_expired_tickets() {
        List<Ticket> emptyList = Lists.emptyList();
        TicketRepository ticketRepository = mock(TicketRepository.class);
        when(ticketRepository.findTicketsInPast(LocalDate.now())).thenReturn(emptyList);
        doNothing().when(ticketRepository).deleteAll(emptyList);
        doNothing().when(ticketRepository).flush();

        TicketInvalidationStage stage = new TicketInvalidationStage(ticketRepository);
        StageResult result = stage.call();
        assertEquals(0, result.getDeletedObjects());
        verify(ticketRepository, times(1)).findTicketsInPast(LocalDate.now());
        verify(ticketRepository, never()).deleteAll(emptyList);
        verify(ticketRepository, never()).flush();
    }
}