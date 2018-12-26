package com.example.tickets.service;

import com.example.tickets.exception.ServiceException;
import com.example.tickets.service.aviasales.AviasalesService;
import com.google.common.collect.Multimap;
import com.google.common.collect.TreeMultimap;
import lombok.extern.java.Log;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Period;
import java.util.*;
import java.util.stream.Collectors;

import static com.example.tickets.util.DateConverter.toLocalDate;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Log
public class AviasalesServiceTest {
    @Autowired
    private AviasalesService aviasalesService;

    @Autowired
    private ModelMapper mapper;

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void oneWayTicketPricesCanBeReceived() throws ServiceException {
        List<TicketDTO> ticketsForNextWeek = aviasalesService.getOneWayTicket("LED", "MOW", LocalDate.now(), 7);
        assertNotNull(ticketsForNextWeek);
        assertThat(ticketsForNextWeek, hasSize(greaterThanOrEqualTo(1)));
        log.info("Found tickets: ");
        ticketsForNextWeek.forEach(ticket -> log.info(ticket.toString()));
        log.info("Cheapest ticket from LED to MOW for a next week: ");
        ticketsForNextWeek.sort(Comparator.comparing(TicketDTO::getValue));
        Optional<TicketDTO> firstOptional = ticketsForNextWeek.stream().findFirst();
        assertTrue(firstOptional.isPresent());
        log.info(firstOptional.get().toString());
    }

    @Test
    public void returnTicketPricesCanBeReceived() throws ServiceException {
        var today = LocalDate.now();
        var threeWeeksLater = today.plus(Period.ofWeeks(3));
        List<TicketDTO> ticketsForNextWeek = aviasalesService.getReturnTicket("LED", "MOW", today, threeWeeksLater, 7, 7);
        assertNotNull(ticketsForNextWeek);
        assertThat(ticketsForNextWeek, hasSize(greaterThanOrEqualTo(1)));
        log.info("Found tickets: ");
        ticketsForNextWeek.forEach(ticket -> log.info(ticket.toString()));
        log.info("Cheapest return tickets from LED to MOW in a 3 weeks: ");
        ticketsForNextWeek.sort(Comparator.comparing(TicketDTO::getValue));
        Optional<TicketDTO> firstOptional = ticketsForNextWeek.stream().findFirst();
        assertTrue(firstOptional.isPresent());
        log.info(firstOptional.get().toString());
    }

    @Test
    public void findCheapestFridayToSundayTicketsInAThreeMonths() throws ServiceException {
        var today = LocalDate.now();
        var inAThreeMonths = today.plusMonths(3);
        var from = "LED";
        var to = "MOW";

        List<LocalDate> departureFridays = today
                .datesUntil(inAThreeMonths)
                .filter(e -> e.getDayOfWeek() == DayOfWeek.FRIDAY)
                .collect(Collectors.toList());

        List<TicketDTO> fridayTickets = Collections.synchronizedList(new ArrayList<>());
        departureFridays
                .parallelStream()
                .forEach(friday -> {
                    List<TicketDTO> foundFridayTickets = aviasalesService.getOneWayTicket(from, to, friday, 1);
                    fridayTickets.addAll(foundFridayTickets);
                });
        DescriptiveStatistics fridayDS = new DescriptiveStatistics();
        fridayTickets.forEach(t -> fridayDS.addValue(t.getValue()));

        List<LocalDate> returnSundays = today
                .datesUntil(inAThreeMonths)
                .filter(e -> e.getDayOfWeek() == DayOfWeek.SUNDAY)
                .collect(Collectors.toList());

        List<TicketDTO> sundayTickets = Collections.synchronizedList(new ArrayList<>());
        returnSundays
                .parallelStream()
                .forEach(sunday -> {
                    List<TicketDTO> foundSundayTickets = aviasalesService.getOneWayTicket(to, from, sunday, 1);
                    sundayTickets.addAll(foundSundayTickets);
                });
        DescriptiveStatistics sundayDS = new DescriptiveStatistics();
        sundayTickets.forEach(t -> sundayDS.addValue(t.getValue()));

        Multimap<TicketDTO, TicketDTO> ticketPairs = TreeMultimap.create(Comparator.comparing(TicketDTO::getValue), Comparator.comparing(TicketDTO::getValue));
        for (TicketDTO fridayTicket : fridayTickets) {
            for (TicketDTO sundayTicket : sundayTickets) {
                var departDate = toLocalDate(fridayTicket.getDepart_date());
                var returnDate = toLocalDate(sundayTicket.getDepart_date());
                if (returnDate.isAfter(departDate) && departDate.until(returnDate).getDays() == 2) {
                    ticketPairs.put(fridayTicket, sundayTicket);
                }

            }
        }

        Optional<TicketDTO> cheapestFridayTicketOpt = ticketPairs.keySet().stream().findFirst();
        assertTrue(cheapestFridayTicketOpt.isPresent());
        TicketDTO cheapestFridayTicket = cheapestFridayTicketOpt.get();

        Optional<TicketDTO> cheapestSundayTicketOpt = ticketPairs.get(cheapestFridayTicket).stream().findFirst();
        assertTrue(cheapestSundayTicketOpt.isPresent());
        TicketDTO cheapestSundayTicket = cheapestSundayTicketOpt.get();

        log.info("Cheapest friday ticket: " + cheapestFridayTicket);
        log.info("Cheapest sunday ticket: " + cheapestSundayTicket);

        log.info("Cheapest friday price: " + cheapestFridayTicket.getValue());
        log.info("Cheapest sunday price: " + cheapestSundayTicket.getValue());
        log.info("Cheapest total price: " + (cheapestFridayTicket.getValue() + cheapestSundayTicket.getValue()));

        double fridayTicketsMean = fridayDS.getMean();
        log.info("Friday mean value: " + fridayTicketsMean);
        double fridayTickets25Percentile = fridayDS.getPercentile(25);
        log.info("Friday 25 percentile value: " + fridayTickets25Percentile);
        double sundayTicketsMean = fridayDS.getMean();
        log.info("Sunday mean value: " + sundayTicketsMean);
        double sundayTickets25Percentile = sundayDS.getPercentile(25);
        log.info("Sunday 25 percentile value: " + sundayTickets25Percentile);

        double fridayCheapestMeanDiff = Math.round(100 - (cheapestFridayTicket.getValue() / fridayTicketsMean * 100));
        log.info("Cheapest friday ticket is cheaper than mean price by " + fridayCheapestMeanDiff + "%");
        double sundayCheapestMeanDiff = Math.round(100 - (cheapestSundayTicket.getValue() / sundayTicketsMean * 100));
        log.info("Cheapest sunday ticket is cheaper than mean price by " + sundayCheapestMeanDiff + "%");

        double fridayCheapest25PercentileDiff = Math.round(100 - (cheapestFridayTicket.getValue() / fridayTickets25Percentile * 100));
        log.info("Cheapest friday ticket is cheaper than 25 percentile price by " + fridayCheapest25PercentileDiff + "%");
        double sundayCheapest25PercentileDiff = Math.round(100 - (cheapestSundayTicket.getValue() / sundayTickets25Percentile * 100));
        log.info("Cheapest sunday ticket is cheaper than 25 percentile price by " + sundayCheapest25PercentileDiff + "%");
    }


}