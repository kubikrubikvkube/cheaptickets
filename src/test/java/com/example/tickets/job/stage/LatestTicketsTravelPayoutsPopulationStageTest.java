package com.example.tickets.job.stage;

import com.example.tickets.subscription.SubscriptionService;
import com.example.tickets.ticket.Ticket;
import com.example.tickets.ticket.TicketService;
import com.example.tickets.travelpayouts.TravelPayoutsService;
import com.example.tickets.travelpayouts.request.LatestRequest;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.apache.commons.lang3.NotImplementedException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class LatestTicketsTravelPayoutsPopulationStageTest {

    @Test
    void if_ticket_not_exist_ticket_should_be_saved() {
        TravelPayoutsService travelPayoutsService = mock(TravelPayoutsService.class);
        SubscriptionService subscriptionService = mock(SubscriptionService.class);
        TicketService ticketService = mock(TicketService.class);

        LatestRequest ledMowLatestRequest = LatestRequest.builder()
                .origin("A")
                .destination("B")
                .limit(1000)
                .one_way(true)
                .build();

        Ticket ticket = new Ticket();
        List<Ticket> ticketList = List.of(ticket);
        when(travelPayoutsService.getLatest(ledMowLatestRequest)).thenReturn(ticketList);

        Multimap<String, String> subscriptionDestinations = ArrayListMultimap.create(1, 1);
        subscriptionDestinations.put("A", "B");
        when(subscriptionService.findDistinctOriginAndDestination()).thenReturn(subscriptionDestinations);

        when(ticketService.exist(ticket)).thenReturn(false);

        LatestTicketsTravelPayoutsPopulationStage stage = new LatestTicketsTravelPayoutsPopulationStage(travelPayoutsService, subscriptionService, ticketService);
        StageResult call = stage.call();
        assertEquals(1, call.getSavedObjects());
    }

    @Test
    void if_ticket_not_exist_ticket_should_not_be_saved() {
        TravelPayoutsService travelPayoutsService = mock(TravelPayoutsService.class);
        SubscriptionService subscriptionService = mock(SubscriptionService.class);
        TicketService ticketService = mock(TicketService.class);

        LatestRequest ledMowLatestRequest = LatestRequest.builder()
                .origin("A")
                .destination("B")
                .limit(1000)
                .one_way(true)
                .build();

        Ticket ticket = new Ticket();
        List<Ticket> ticketList = List.of(ticket);
        when(travelPayoutsService.getLatest(ledMowLatestRequest)).thenReturn(ticketList);


        Multimap<String, String> subscriptionDestinations = ArrayListMultimap.create(1, 1);
        subscriptionDestinations.put("A", "B");
        when(subscriptionService.findDistinctOriginAndDestination()).thenReturn(subscriptionDestinations);

        when(ticketService.exist(ticket)).thenReturn(true);

        LatestTicketsTravelPayoutsPopulationStage stage = new LatestTicketsTravelPayoutsPopulationStage(travelPayoutsService, subscriptionService, ticketService);
        StageResult call = stage.call();
        assertEquals(0, call.getSavedObjects());
    }

    @Test
    void ticketservice_exist_method_should_compare_tickets_as_expected() {
        throw new NotImplementedException("Test is not ready yet");
    }
}