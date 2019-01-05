package com.example.tickets.job;

import com.example.tickets.ticket.TicketRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TicketInvalidationJobTest {
    Logger log = LoggerFactory.getLogger(TicketInvalidationJobTest.class);
    @Autowired
    private TicketRepository ticketRepository;

    @Before
    public void setUp() {

    }

    @After
    public void tearDown() {

    }

    @Test
    public void ticketInvalidationJobWorks() throws SchedulerException, InterruptedException {

    }
}
