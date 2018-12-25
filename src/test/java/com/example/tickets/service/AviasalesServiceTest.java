package com.example.tickets.service;

import com.example.tickets.exception.ServiceException;
import com.example.tickets.ticket.TicketJson;
import com.google.common.collect.Multimap;
import com.google.common.collect.TreeMultimap;
import lombok.extern.java.Log;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

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
    public void oneWayTicketPricesCanBeReceived() throws ServiceException {
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

    @Test
    public void returnTicketPricesCanBeReceived() throws ServiceException {
        var today = LocalDate.now();
        var threeWeeksLater = today.plus(Period.ofWeeks(3));
        List<TicketJson> ticketsForNextWeek = aviasalesService.getReturnTicket("LED", "MOW", today, threeWeeksLater, 7, 7);
        assertNotNull(ticketsForNextWeek);
        assertThat(ticketsForNextWeek, hasSize(greaterThanOrEqualTo(1)));
        log.info("Found tickets: ");
        ticketsForNextWeek.forEach(ticket -> log.info(ticket.toString()));
        log.info("Cheapest return tickets from LED to MOW in a 3 weeks: ");
        ticketsForNextWeek.sort(Comparator.comparing(TicketJson::getValue));
        Optional<TicketJson> firstOptional = ticketsForNextWeek.stream().findFirst();
        assertTrue(firstOptional.isPresent());
        log.info(firstOptional.get().toString());
    }

    @Test
    public void findCheapestFridayToSundayTicketsInAThreeMonths() throws ServiceException {
        var today = LocalDate.now();
        var inAThreeMonths = today.plusMonths(3);

        List<LocalDate> departureFridays = today
                .datesUntil(inAThreeMonths)
                .filter(e -> e.getDayOfWeek() == DayOfWeek.FRIDAY)
                .collect(Collectors.toList());

        List<LocalDate> returnSundays = today
                .datesUntil(inAThreeMonths)
                .filter(e -> e.getDayOfWeek() == DayOfWeek.SUNDAY)
                .collect(Collectors.toList());

        var from = "LED";
        var to = "MOW";
        List<TicketJson> fridayTickets = Collections.synchronizedList(new ArrayList<>());
        departureFridays
                .parallelStream()
                .forEach(friday -> {
                    List<TicketJson> foundFridayTickets = aviasalesService.getOneWayTicket(from, to, friday, 1);
                    fridayTickets.addAll(foundFridayTickets);
                });

        List<TicketJson> sundayTickets = Collections.synchronizedList(new ArrayList<>());
        returnSundays
                .parallelStream()
                .forEach(sunday -> {
                    List<TicketJson> foundSundayTickets = aviasalesService.getOneWayTicket(to, from, sunday, 1);
                    sundayTickets.addAll(foundSundayTickets);
                });

        fridayTickets.sort(Comparator.comparing(TicketJson::getValue));
        sundayTickets.sort(Comparator.comparing(TicketJson::getValue));


        Multimap<TicketJson, TicketJson> ticketPairs = TreeMultimap.create(Comparator.comparing(TicketJson::getValue), Comparator.comparing(TicketJson::getValue));
        for (TicketJson fridayTicket : fridayTickets) {
            for (TicketJson sundayTicket : sundayTickets) {
                var departDate = LocalDate.ofInstant(fridayTicket.getDepart_date().toInstant(), ZoneId.systemDefault());
                var returnDate = LocalDate.ofInstant(sundayTicket.getDepart_date().toInstant(), ZoneId.systemDefault());
                if (returnDate.isAfter(departDate) && departDate.until(returnDate).getDays() == 2) {
                    ticketPairs.put(fridayTicket, sundayTicket);
                }

            }
        }

        Optional<TicketJson> cheapestFridayTicketOpt = ticketPairs.keySet().stream().findFirst();
        assertTrue(cheapestFridayTicketOpt.isPresent());
        TicketJson cheapestFridayTicket = cheapestFridayTicketOpt.get();
        Optional<TicketJson> cheapestSundayTicketOpt = ticketPairs.get(cheapestFridayTicket).stream().findFirst();
        assertTrue(cheapestSundayTicketOpt.isPresent());
        TicketJson cheapestSundayTicket = cheapestSundayTicketOpt.get();
        log.info("Cheapest friday ticket: " + cheapestFridayTicket);
        log.warning("Cheapest sunday ticket: " + cheapestSundayTicket);
        int debug = 0;

    }


}