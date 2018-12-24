package com.example.tickets;

import com.example.tickets.request.LatestRequest;
import com.example.tickets.ticket.Ticket;
import com.example.tickets.ticket.TicketRepository;
import com.example.tickets.ticket.TicketService;
import com.example.tickets.ticket.TicketServiceException;
import lombok.extern.java.Log;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static com.example.tickets.request.Sorting.PRICE;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@Log
public class LatestTicketRepositoryTest {
    @Autowired
    private TicketService ticketService;
    @Autowired
    private TicketRepository ticketRepository;

    @Test
    public void shouldGetTicketsAndSaveThemToDB() throws TicketServiceException {
        LatestRequest priceSorting = LatestRequest.builder()
                .origin("LED")
                .destination("DME")
                .sorting(PRICE)
                .show_to_affiliates(false)
                .limit(5)
                .build();
        List<Ticket> byPrice = ticketService.getLatest(priceSorting);
        log.info("Got sorted tickets: " + byPrice);
        Iterable<Ticket> savedTickets = ticketRepository.saveAll(byPrice);
        log.info("Saved tickets: " + savedTickets);
        List<Long> ids = new ArrayList<>();
        savedTickets.forEach(ticket -> ids.add(ticket.getId()));

        Iterable<Ticket> allById = ticketRepository.findAllById(ids);
        assertEquals(savedTickets, allById);
        ticketRepository.deleteAll(allById);
    }
}
