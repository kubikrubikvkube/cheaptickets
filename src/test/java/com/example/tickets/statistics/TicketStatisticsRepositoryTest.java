package com.example.tickets.statistics;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class TicketStatisticsRepositoryTest {
    @Autowired
    private TicketStatisticsRepository repository;

    @BeforeEach
    public void setUp() throws Exception {
    }

    @AfterEach
    public void tearDown() throws Exception {
    }

    @Test
    public void crud() throws Exception {
        TicketStatistics ts = new TicketStatistics();
        ts.setOrigin("LED");
        ts.setDestination("MOW");
        ts.setTicketStatisticsByDay(Collections.emptyList());
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