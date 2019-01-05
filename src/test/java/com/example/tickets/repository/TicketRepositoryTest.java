package com.example.tickets.repository;

import com.example.tickets.ticket.Ticket;
import com.example.tickets.ticket.TicketRepository;
import com.example.tickets.travelpayouts.TravelPayoutsService;
import com.example.tickets.travelpayouts.request.LatestRequest;
import com.example.tickets.util.DateConverter;
import com.example.tickets.util.ServiceException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.example.tickets.travelpayouts.request.Sorting.DISTANCE_UNIT_PRICE;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TicketRepositoryTest {
    private final Logger log = LoggerFactory.getLogger(TicketRepositoryTest.class);
    @Autowired
    private TravelPayoutsService travelPayoutsService;
    @Autowired
    private TicketRepository ticketRepository;

    private LatestRequest someRequest;

    @Before
    public void setUp() {
        LocalDate now = LocalDateTime.now().toLocalDate();
        LocalDate nextMonth = now.plusMonths(1);
        int dayOfMonth = nextMonth.getDayOfMonth();
        LocalDate firstDayOfNextMonth = nextMonth.minusDays(dayOfMonth).plusDays(1);


        someRequest = LatestRequest.builder()
                .origin("LED")
                .destination("DME")
                .period_type("month")
                .beginning_of_period(firstDayOfNextMonth)
                .sorting(DISTANCE_UNIT_PRICE)
                .show_to_affiliates(false)
                .limit(5)
                .build();
    }

    @Test
    public void existsByBasicData() {
        boolean shouldNotExist = ticketRepository.existsByOriginAndDestinationAndDepartDateAndValue("MOW", "LED", DateConverter.toDate(LocalDate.now()), 1);
        assertFalse(shouldNotExist);
        List<Ticket> latest = travelPayoutsService.getLatest(someRequest);
        assertThat(latest, hasSize(greaterThanOrEqualTo(1)));
        Ticket ticket = latest.get(0);
        ticketRepository.save(ticket);
        boolean shouldExist = ticketRepository.existsByOriginAndDestinationAndDepartDateAndValue(ticket.getOrigin(), ticket.getDestination(), ticket.getDepartDate(), ticket.getValue());
        assertTrue(shouldExist);
        ticketRepository.delete(ticket);
    }

    @Test
    public void shouldGetTicketsAndSaveThemToDB() throws ServiceException {
        List<Ticket> byPrice = travelPayoutsService.getLatest(someRequest);
        log.info("Got tickets: " + byPrice);
        Iterable<Ticket> savedTickets = ticketRepository.saveAll(byPrice);
        log.info("Saved tickets: " + savedTickets);
        ticketRepository.deleteAll(savedTickets);
    }

    @Test
    public void shouldGetTicketsByDateAndSaveThemToDB() throws ServiceException {
        List<Ticket> byPrice = travelPayoutsService.getLatest(someRequest);
        log.info("Got sorted tickets: " + byPrice);
        Iterable<Ticket> savedTickets = ticketRepository.saveAll(byPrice);
        log.info("Saved tickets: " + savedTickets);
        List<Ticket> byDepartDate = ticketRepository.findByDepartDate(byPrice.get(0).getDepartDate());
        //нужно проверить что мы сохранили больше одного билета
        //взять его дату, найти в базе, узнать что найдено большое одной записи
        //сверить что этот билет есть среди тех, которые мы сохраняли
        //удалить все сохраненные по айдишнику
        assertNotNull(byDepartDate);
        assertThat(byDepartDate, not(empty()));
        log.info("Found by date: " + byDepartDate);
        ticketRepository.deleteAll(savedTickets);
    }

    @Test
    public void shouldBeRequestByBasicData() throws ServiceException {
        List<Ticket> byPrice = travelPayoutsService.getLatest(someRequest);
        Optional<Ticket> byPriceEntityOpt = byPrice.stream().findFirst();
        assertTrue(byPriceEntityOpt.isPresent());
        Ticket ticket = byPriceEntityOpt.get();
        log.info("Got ticket: " + ticket);

        ticketRepository.save(ticket);

        String origin = ticket.getOrigin();
        String destination = ticket.getDestination();
        Date departDate = ticket.getDepartDate();
        Integer value = ticket.getValue();

        List<Ticket> byBasicData = ticketRepository.findByOriginAndDestinationAndDepartDateAndValue(origin, destination, departDate, value);
        assertNotNull(byBasicData);
        assertThat(byBasicData, hasSize(1));
        Ticket fetchedTicket = byBasicData.get(0);
        assertEquals(ticket, fetchedTicket);
        ticketRepository.delete(ticket);
    }

    @Test
    public void equalEntitiesShouldBeEqualToEachOther() {
        List<Ticket> tickets = travelPayoutsService.getLatest(someRequest);
        assertThat(tickets, hasSize(greaterThanOrEqualTo(1)));
        Ticket ticket = tickets.get(0);
        Ticket savedEntity = ticketRepository.save(ticket);
        assertEquals(ticket, savedEntity);
        ticketRepository.delete(savedEntity);

    }
}
