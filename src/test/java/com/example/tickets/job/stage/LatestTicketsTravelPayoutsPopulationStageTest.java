package com.example.tickets.job.stage;

import com.example.tickets.subscription.SubscriptionService;
import com.example.tickets.ticket.Ticket;
import com.example.tickets.ticket.TicketService;
import com.example.tickets.travelpayouts.TravelPayoutsService;
import com.example.tickets.travelpayouts.request.LatestRequest;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class LatestTicketsTravelPayoutsPopulationStageTest {
    private final Logger log = LoggerFactory.getLogger(LatestTicketsTravelPayoutsPopulationStageTest.class);
    @SpyBean
    private TravelPayoutsService travelPayoutsService;

    @SpyBean
    private SubscriptionService subscriptionService;

    @SpyBean
    private TicketService ticketService;

    private LatestRequest fromAToBLatestRequest;

    @BeforeEach
    void setUp() {
        fromAToBLatestRequest = LatestRequest.builder()
                .origin("A")
                .destination("B")
                .limit(1000)
                .one_way(true)
                .build();
    }

    @Test
    @Disabled
    void if_ticket_not_exist_ticket_should_be_saved() {
        Ticket ticket = new Ticket();
        List<Ticket> ticketList = List.of(ticket);
        when(travelPayoutsService.getLatest(fromAToBLatestRequest)).thenReturn(ticketList);

        Multimap<String, String> subscriptionDestinations = ArrayListMultimap.create(1, 1);
        subscriptionDestinations.put("A", "B");
        when(subscriptionService.findDistinctOriginAndDestination()).thenReturn(subscriptionDestinations);

        when(ticketService.exist(ticket)).thenReturn(false);

        LatestTicketsTravelPayoutsPopulationStage stage = new LatestTicketsTravelPayoutsPopulationStage(travelPayoutsService, subscriptionService, ticketService);
        StageResult stageResult = stage.call();
        log.info("{}", stageResult);
        assertEquals(1, stageResult.getSavedObjects());
    }

    @Test
    @Disabled
    void if_ticket_not_exist_ticket_should_not_be_saved() {
        Ticket ticket = new Ticket();
        List<Ticket> ticketList = List.of(ticket);
        when(travelPayoutsService.getLatest(fromAToBLatestRequest)).thenReturn(ticketList);


        Multimap<String, String> subscriptionDestinations = ArrayListMultimap.create(1, 1);
        subscriptionDestinations.put("A", "B");
        when(subscriptionService.findDistinctOriginAndDestination()).thenReturn(subscriptionDestinations);

        when(ticketService.exist(ticket)).thenReturn(true);

        LatestTicketsTravelPayoutsPopulationStage stage = new LatestTicketsTravelPayoutsPopulationStage(travelPayoutsService, subscriptionService, ticketService);
        StageResult stageResult = stage.call();
        log.info("{}", stageResult);
        assertEquals(0, stageResult.getSavedObjects());
    }

    @Test
    @Disabled
    void exist_method_should_compare_saved_tickets_as_expected() {
        Ticket complexTicket = new Ticket();
        complexTicket.setOrigin("A");
        complexTicket.setDestination("B");
        complexTicket.setValue(1234567);
        complexTicket.setFlightNumber("C");
        complexTicket.setDepartDate(LocalDate.now());
        complexTicket.setDepartTime(LocalTime.now());
        ticketService.save(complexTicket);

        List<Ticket> ticketList = List.of(complexTicket);
        when(travelPayoutsService.getLatest(fromAToBLatestRequest)).thenReturn(ticketList);
        Multimap<String, String> subscriptionDestinations = ArrayListMultimap.create(1, 1);
        subscriptionDestinations.put("A", "B");
        when(subscriptionService.findDistinctOriginAndDestination()).thenReturn(subscriptionDestinations);

        LatestTicketsTravelPayoutsPopulationStage stage = new LatestTicketsTravelPayoutsPopulationStage(travelPayoutsService, subscriptionService, ticketService);
        StageResult stageResult = stage.call();
        log.info("{}", stageResult);
        assertEquals(0, stageResult.getSavedObjects());
    }
}