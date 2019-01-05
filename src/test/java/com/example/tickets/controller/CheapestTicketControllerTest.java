package com.example.tickets.controller;

import com.example.tickets.ticket.Ticket;
import com.example.tickets.ticket.TicketRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class CheapestTicketControllerTest {
    @Autowired
    TicketRepository ticketRepository;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private CheapestTicketController controller;
    private Ticket t;
    private final LocalDateTime date = LocalDate.now().plusDays(3).atStartOfDay();

    @Before
    public void setUp() {
        ZoneOffset localOffset = ZoneOffset.systemDefault().getRules().getOffset(Instant.now());
        t = new Ticket();
        t.setOrigin("LED");
        t.setDestination("MOW");
        t.setDepartDate(date.atOffset(localOffset));
        ticketRepository.save(t);
    }

    @After
    public void tearDown() {
        ticketRepository.delete(t);
    }

    @Test
    public void cheapest() throws Exception {
        String request = "/cheapest?origin=LED&destination=MOW&departureDate=" + date;
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        String ticketJsonString = mapper.writerFor(Ticket.class).writeValueAsString(t);
        this.mockMvc
                .perform(get(request))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(ticketJsonString));
    }
}