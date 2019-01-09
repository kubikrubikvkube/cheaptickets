package com.example.tickets.statistics;

import org.assertj.core.util.Lists;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TicketStatisticsRepositoryTest {
    @Autowired
    private TicketStatisticsRepository repository;

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void crud() throws Exception {
        TicketStatistics ticketStatistics = new TicketStatistics();
        ticketStatistics.setOrigin("LED");
        ticketStatistics.setDestination("MOW");
        ticketStatistics.setTicketStatisticsByDay(Lists.emptyList());
        ticketStatistics.setTicketStatisticsByMonth(Lists.emptyList());
        TicketStatistics save = repository.save(ticketStatistics);
        var id = save.getId();
        Optional<TicketStatistics> byId = repository.findById(id);
        assertTrue(byId.isPresent());
        var fromDB = byId.get();
        assertEquals(ticketStatistics.getOrigin(), fromDB.getOrigin());
        assertEquals(ticketStatistics.getDestination(), fromDB.getDestination());
        fromDB.setOrigin("ROM");
        fromDB.setOrigin("PAR");
        repository.save(fromDB);
        var fromDBUpdated = repository.findById(fromDB.getId());
        assertTrue(fromDBUpdated.isPresent());
        repository.delete(fromDBUpdated.get());
        var fromDBEmpty = repository.findById(fromDB.getId());
        assertFalse(fromDBEmpty.isPresent());

    }
}