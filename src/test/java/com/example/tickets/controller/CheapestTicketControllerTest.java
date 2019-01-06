package com.example.tickets.controller;

import com.example.tickets.ticket.Ticket;
import com.example.tickets.ticket.TicketDTO;
import com.example.tickets.ticket.TicketRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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
    private final LocalDate date = LocalDate.now().plusDays(3);

    private Ticket t;
    private final LocalTime time = LocalTime.now();
    private final Integer value = 12345;
    @Autowired
    private ModelMapper mapper;

    @Before
    public void setUp() {
        TicketDTO dto = new TicketDTO();
        dto.setOrigin("LED");
        dto.setDestination("MOW");
        dto.setDepart_date(date);
        dto.setDepartTime(time);
        dto.setValue(value);
        t = mapper.map(dto, Ticket.class);
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
//        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        String expectedDate = date.toString();
        String expectedTime = time.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        this.mockMvc
                .perform(get(request))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.origin", is("LED")))
                .andExpect(jsonPath("$.destination", is("MOW")))
                .andExpect(jsonPath("$.departDate", is(expectedDate)))
                .andExpect(jsonPath("$.departTime", is(expectedTime)))
                .andExpect(jsonPath("$.value", is(value)));
    }
}