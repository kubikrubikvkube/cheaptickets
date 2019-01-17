package com.example.tickets.controller;

import com.example.tickets.ticket.Ticket;
import com.example.tickets.ticket.TicketDTO;
import com.example.tickets.ticket.TicketDTOMapper;
import com.example.tickets.ticket.TicketRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ExampleTicketControllerTest {
    private final LocalDate date = LocalDate.now().plusDays(3);
    private final LocalTime time = LocalTime.now();
    private final Integer value = 12345;
    @Autowired
    private
    TicketRepository ticketRepository;
    @Autowired
    private MockMvc mockMvc;
    private TicketDTOMapper mapper = TicketDTOMapper.INSTANCE;
    private Ticket t;

    @BeforeEach
    public void setUp() {
        TicketDTO dto = new TicketDTO();
        dto.setOrigin("LED");
        dto.setDestination("MOW");
        dto.setDepart_date(date);
        dto.setDepart_time(time);
        dto.setValue(value);
        t = mapper.fromDTO(dto);
        ticketRepository.save(t);
    }

    @AfterEach
    public void tearDown() {
        ticketRepository.delete(t);
    }

    @Test
    public void cheapest() throws Exception {
        String request = "/cheapest?origin=LED&destination=MOW&departureDate=" + date;
        String expectedDate = date.toString();
        String expectedTime = time.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        this.mockMvc
                .perform(get(request))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.origin", is("LED")))
                .andExpect(jsonPath("$.destination", is("MOW")))
                .andExpect(jsonPath("$.departDate", is(expectedDate)))
                .andExpect(jsonPath("$.depart_time", is(expectedTime)))
                .andExpect(jsonPath("$.value", is(value)));
    }
}