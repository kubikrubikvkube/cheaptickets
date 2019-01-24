package com.example.tickets.job.stage;

import com.example.tickets.aviasales.AviasalesService;
import com.example.tickets.subscription.SubscriptionService;
import com.example.tickets.ticket.Ticket;
import com.example.tickets.ticket.TicketService;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class OnewayTicketsForAYearAviasalesStageTest {
    private final Logger log = LoggerFactory.getLogger(OnewayTicketsForAYearAviasalesStageTest.class);
    @Mock
    private AviasalesService aviasalesService;

    @Mock
    private SubscriptionService subscriptionService;

    @Autowired
    private TicketService ticketService;

    @Test
    void should_save_new_ticket_to_database() {
        Multimap<String, String> multimap = ArrayListMultimap.create(1, 1);
        multimap.put("a", "b");
        when(subscriptionService.findDistinctOriginAndDestination()).thenReturn(multimap);
        Ticket ticket = new Ticket();
        when(aviasalesService.getOneWayTicket("a", "b", LocalDate.now(), 1)).thenReturn(List.of(ticket));

        OnewayTicketsForAYearAviasalesStage stage = new OnewayTicketsForAYearAviasalesStage(ticketService, subscriptionService, aviasalesService);
        StageResult stageResult = stage.call();
        assertEquals(1, stageResult.getSavedObjects());
    }

    @Test
    void should_not_save_new_ticket_to_database_if_already_exist() {
        Multimap<String, String> multimap = ArrayListMultimap.create(1, 1);
        multimap.put("a", "b");
        when(subscriptionService.findDistinctOriginAndDestination()).thenReturn(multimap);
        Ticket ticket = new Ticket();
        ticket.setOrigin("a");
        ticket.setDestination("b");
        ticket.setValue(1);
        ticketService.save(ticket);
        when(aviasalesService.getOneWayTicket("a", "b", LocalDate.now(), 1)).thenReturn(List.of(ticket));

        OnewayTicketsForAYearAviasalesStage stage = new OnewayTicketsForAYearAviasalesStage(ticketService, subscriptionService, aviasalesService);
        StageResult stageResult = stage.call();
        assertEquals(0, stageResult.getSavedObjects());
    }
}