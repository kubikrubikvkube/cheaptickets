package com.example.tickets.service;

import com.example.tickets.exception.ServiceException;
import com.example.tickets.ticket.TicketJson;
import lombok.extern.java.Log;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Log
public class AviasalesServiceTest {
    @Autowired
    AviasalesService aviasalesService;

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void oneWayPricesCanBeReceived() throws ServiceException {
        List<TicketJson> ticketsForNextWeek = aviasalesService.getOneWayTicket("LED", "MOW", LocalDate.now(), 7);
        assertNotNull(ticketsForNextWeek);
        assertThat(ticketsForNextWeek, hasSize(greaterThanOrEqualTo(1)));
        log.info("Found tickets: ");
        ticketsForNextWeek.forEach(ticket -> log.info(ticket.toString()));
        log.info("Cheapest ticket from LED to MOW for a next week: ");
        ticketsForNextWeek.sort(Comparator.comparing(TicketJson::getValue));
        Optional<TicketJson> firstOptional = ticketsForNextWeek.stream().findFirst();
        assertTrue(firstOptional.isPresent());
        log.info(firstOptional.get().toString());

    }


}