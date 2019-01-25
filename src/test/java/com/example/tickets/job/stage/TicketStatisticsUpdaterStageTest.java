package com.example.tickets.job.stage;

import com.example.tickets.owner.Owner;
import com.example.tickets.statistics.TicketStatistics;
import com.example.tickets.statistics.TicketStatisticsByMonth;
import com.example.tickets.statistics.TicketStatisticsByMonthDTOMapper;
import com.example.tickets.statistics.TicketStatisticsService;
import com.example.tickets.subscription.Subscription;
import com.example.tickets.subscription.SubscriptionService;
import com.example.tickets.ticket.Ticket;
import com.example.tickets.ticket.TicketService;
import com.google.common.collect.Lists;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class TicketStatisticsUpdaterStageTest {

    private static Subscription simpleSubscription;
    private static Ticket simpleTicket;
    private static Ticket expensiveTicket;
    @SpyBean
    private TicketStatisticsService ticketStatisticsService;
    @SpyBean
    private TicketService ticketService;
    @SpyBean
    private SubscriptionService subscriptionService;
    @SpyBean
    private TicketStatisticsByMonthDTOMapper dtoMapper;

    @BeforeAll
    static void setUp() {
        simpleSubscription = new Subscription();
        simpleSubscription.setOrigin("a");
        simpleSubscription.setDestination("b");
        simpleSubscription.setOwner(new Owner());
        simpleSubscription.setDepartDate(LocalDate.now());
        simpleSubscription.setReturnDate(LocalDate.now().plusDays(1));

        simpleTicket = new Ticket();
        simpleTicket.setOrigin("a");
        simpleTicket.setDestination("b");
        simpleTicket.setDepartDate(LocalDate.now());
        simpleTicket.setValue(1000);
        simpleTicket.setNumberOfChanges(1);

        expensiveTicket = new Ticket();
        expensiveTicket.setOrigin("a");
        expensiveTicket.setDestination("b");
        expensiveTicket.setDepartDate(LocalDate.now());
        expensiveTicket.setValue(3000);
        expensiveTicket.setNumberOfChanges(1);
    }

    @Test
    void should_generate_correct_statistics_for_1_ticket() {
        List<Ticket> ticketList = Lists.newArrayList(simpleTicket);
        when(subscriptionService.findAll()).thenReturn(List.of(simpleSubscription));
        when(ticketService.findBySubscription(simpleSubscription)).thenReturn(ticketList);

        TicketStatisticsUpdaterStage stage = new TicketStatisticsUpdaterStage(ticketStatisticsService, ticketService, subscriptionService, dtoMapper);
        StageResult stageResult = stage.call();
        assertEquals(1, stageResult.getUpdatedObjects());
        Optional<TicketStatistics> ticketStatisticsOptional = ticketStatisticsService.findByOriginAndDestination("a", "b");
        assertTrue(ticketStatisticsOptional.isPresent());
        TicketStatistics ticketStatistics = ticketStatisticsOptional.get();
        assertNotNull(ticketStatistics.getId());
        assertEquals("a", ticketStatistics.getOrigin());
        assertEquals("b", ticketStatistics.getDestination());
        assertThat(ticketStatistics.getTicketStatisticsByMonth(), hasSize(1));
        assertNotNull(ticketStatistics.getCreatedAt());
        assertNotNull(ticketStatistics.getUpdatedAt());
        Optional<TicketStatisticsByMonth> ticketStatisticsByMonthOptional = ticketStatistics.getTicketStatisticsByMonth().stream().findFirst();
        assertTrue(ticketStatisticsByMonthOptional.isPresent());
        TicketStatisticsByMonth ticketStatisticsByMonth = ticketStatisticsByMonthOptional.get();
        assertNotNull(ticketStatisticsByMonth.getId());
        assertNotNull(ticketStatisticsByMonth.getMonth());
        assertEquals(1, ticketStatisticsByMonth.getTicketsCount());
        assertEquals(Double.valueOf(1000), ticketStatisticsByMonth.getMinTicketPrice());
        assertEquals(Double.valueOf(1000), ticketStatisticsByMonth.getAvgTicketPrice());
        assertEquals(Double.valueOf(1000), ticketStatisticsByMonth.getPercentile10());
        assertNotNull(ticketStatisticsByMonth.getCreatedAt());
        assertNotNull(ticketStatisticsByMonth.getUpdatedAt());
    }

    @Test
    void should_generate_correct_statistics_for_2_tickets() {
        List<Ticket> ticketList = Lists.newArrayList(simpleTicket, expensiveTicket);
        when(subscriptionService.findAll()).thenReturn(List.of(simpleSubscription));
        when(ticketService.findBySubscription(simpleSubscription)).thenReturn(ticketList);

        TicketStatisticsUpdaterStage stage = new TicketStatisticsUpdaterStage(ticketStatisticsService, ticketService, subscriptionService, dtoMapper);
        StageResult stageResult = stage.call();
        assertEquals(1, stageResult.getUpdatedObjects());
        Optional<TicketStatistics> ticketStatisticsOptional = ticketStatisticsService.findByOriginAndDestination("a", "b");
        assertTrue(ticketStatisticsOptional.isPresent());
        TicketStatistics ticketStatistics = ticketStatisticsOptional.get();
        assertNotNull(ticketStatistics.getId());
        assertEquals("a", ticketStatistics.getOrigin());
        assertEquals("b", ticketStatistics.getDestination());
        assertThat(ticketStatistics.getTicketStatisticsByMonth(), hasSize(1));
        assertNotNull(ticketStatistics.getCreatedAt());
        assertNotNull(ticketStatistics.getUpdatedAt());
        Optional<TicketStatisticsByMonth> ticketStatisticsByMonthOptional = ticketStatistics.getTicketStatisticsByMonth().stream().findFirst();
        assertTrue(ticketStatisticsByMonthOptional.isPresent());
        TicketStatisticsByMonth ticketStatisticsByMonth = ticketStatisticsByMonthOptional.get();
        assertNotNull(ticketStatisticsByMonth.getId());
        assertNotNull(ticketStatisticsByMonth.getMonth());
        assertEquals(2, ticketStatisticsByMonth.getTicketsCount());
        assertEquals(Double.valueOf(1000), ticketStatisticsByMonth.getMinTicketPrice());
        assertEquals(Double.valueOf(2000), ticketStatisticsByMonth.getAvgTicketPrice());
        assertEquals(Double.valueOf(1000), ticketStatisticsByMonth.getPercentile10());
        assertNotNull(ticketStatisticsByMonth.getCreatedAt());
        assertNotNull(ticketStatisticsByMonth.getUpdatedAt());
    }
}