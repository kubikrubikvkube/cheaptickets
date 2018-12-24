package com.example.tickets;

import com.example.tickets.request.LatestRequest;
import com.example.tickets.request.Sorting;
import com.example.tickets.ticket.*;
import lombok.extern.java.Log;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
        List<TicketEntity> byPriceEntity = byPrice.stream().map(Ticket::toTicketEntity).collect(Collectors.toList());
        log.info("Got sorted tickets: " + byPrice);
        Iterable<TicketEntity> savedTickets = ticketRepository.saveAll(byPriceEntity);
        log.info("Saved tickets: " + savedTickets);
        List<Long> ids = new ArrayList<>();
        savedTickets.forEach(ticket -> ids.add(ticket.getId()));

        Iterable<TicketEntity> allById = ticketRepository.findAllById(ids);
        assertEquals(savedTickets, allById);
        ticketRepository.deleteAll(allById);
    }

    @Test
    public void shouldGetTicketsByDateAndSaveThemToDB() throws TicketServiceException {
        LocalDate now = LocalDateTime.now().toLocalDate();
        LocalDate nextMonth = now.plusMonths(1);
        int dayOfMonth = nextMonth.getDayOfMonth();
        LocalDate firstDayOfNextMonth = nextMonth.minusDays(dayOfMonth).plusDays(1);


        LatestRequest priceSorting = LatestRequest.builder()
                .origin("LED")
                .destination("DME")
                .period_type("month")
                .beginning_of_period(firstDayOfNextMonth.toString())
                .sorting(Sorting.DISTANCE_UNIT_PRICE)
                .show_to_affiliates(false)
                .limit(5)
                .build();

        List<Ticket> byPrice = ticketService.getLatest(priceSorting);
        List<TicketEntity> byPriceEntity = byPrice.stream().map(Ticket::toTicketEntity).collect(Collectors.toList());
        log.info("Got sorted tickets: " + byPrice);
        Iterable<TicketEntity> savedTickets = ticketRepository.saveAll(byPriceEntity);
        log.info("Saved tickets: " + savedTickets);
        List<Long> ids = new ArrayList<>();
        savedTickets.forEach(ticket -> ids.add(ticket.getId()));

        Iterable<TicketEntity> allById = ticketRepository.findAllById(ids);
        assertEquals(savedTickets, allById);
        ticketRepository.deleteAll(allById);
    }
}
