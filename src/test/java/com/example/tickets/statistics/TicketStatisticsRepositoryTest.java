package com.example.tickets.statistics;

import org.assertj.core.util.Lists;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

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
        TicketStatistics ts = new TicketStatistics();
        ts.setOrigin("LED");
        ts.setDestination("MOW");
        ts.setTicketStatisticsByDay(Lists.emptyList());
        ts.setTicketStatisticsByMonth(Lists.emptyList());
        var saved = repository.save(ts);
        var byIdOpt = repository.findById(saved.getId());
        assertTrue(byIdOpt.isPresent());
        var byId = byIdOpt.get();
        assertEquals("LED", byId.getOrigin());
        assertEquals("MOW", byId.getDestination());
        byId.setOrigin("ROM");
        byId.setDestination("PAR");
        repository.save(byId);
        var updatedOpt = repository.findById(byId.getId());
        assertTrue(updatedOpt.isPresent());
        var updated = updatedOpt.get();
        assertEquals("ROM", updated.getOrigin());
        assertEquals("PAR", updated.getDestination());
        repository.delete(updated);
        var empty = repository.findById(updated.getId());
        assertFalse(empty.isPresent());

    }
}