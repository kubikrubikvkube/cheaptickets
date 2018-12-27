package com.example.tickets.rest;

import com.example.tickets.repository.Ticket;
import com.example.tickets.repository.TicketRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static com.example.tickets.util.DateConverter.toDate;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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
    private LocalDate date = LocalDate.now().plusDays(3);

    @Before
    public void setUp() throws Exception {
        t = new Ticket();
        t.setOrigin("LED");
        t.setDestination("MOW");
        t.setDepartDate(toDate(date));
        ticketRepository.save(t);
    }

    @After
    public void tearDown() throws Exception {
        ticketRepository.delete(t);
    }

    @Test
    public void cheapest() throws Exception {
        String request = "/cheapest?origin=LED&destination=MOW&departureDate=" + date;

        this.mockMvc
                .perform(get(request))
                .andDo(print()).andExpect(status().isOk());
        //TODO сравнить json'ы
    }
}