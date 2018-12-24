package com.example.tickets;

import com.example.tickets.request.LatestRequest;
import com.example.tickets.request.Sorting;
import com.example.tickets.ticket.Ticket;
import com.example.tickets.ticket.TicketService;
import com.example.tickets.ticket.TicketServiceException;
import lombok.extern.java.Log;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

import static com.example.tickets.request.Sorting.PRICE;
import static java.lang.String.format;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Log
public class LatestTicketServiceTests {
    @Autowired
    private TicketService ticketService;

    @Test
    public void latestPricesRequestShouldBeBuilded() {
        LatestRequest plr = LatestRequest.builder()
                .currency("RUB")
                .origin("LED")
                .destination("DME")
                .beginning_of_period("2018-05-01")
                .period_type("month")
                .one_way(false)
                .page(1)
                .limit(5)
                .show_to_affiliates(true)
                .sorting(PRICE)
                .trip_duration(1)
                .build();
        String plrAsString = plr.toString();
        log.info("LatestRequest created:" + plrAsString);
        assertEquals(plr.getCurrency(), "RUB");
        assertEquals(plr.getOrigin(), "LED");
        assertEquals(plr.getDestination(), "DME");
        assertEquals(plr.getBeginning_of_period(), "2018-05-01");
        assertEquals(plr.getPeriod_type(), "month");
        assertEquals(plr.getOne_way(), false);
        assertEquals(plr.getPage(), Integer.valueOf(1));
        assertEquals(plr.getLimit(), Integer.valueOf(5));
        assertEquals(plr.getShow_to_affiliates(), true);
        assertEquals(plr.getSorting(), PRICE);
        assertEquals(plr.getTrip_duration(), Integer.valueOf(1));
        assertEquals("http://api.travelpayouts.com/v2/prices/latest?currency=RUB&origin=LED&destination=DME&beginning_of_period=2018-05-01&period_type=month&one_way=false&page=1&limit=5&showToAffiliates=true&sorting=price&trip_duration=1", plrAsString);
    }


    @Test
    public void latestPricesResponseShouldBeReceived() throws TicketServiceException {
        LatestRequest plr = LatestRequest.builder()
                .origin("LED")
                .destination("DME")
                .sorting(PRICE)
                .show_to_affiliates(true)
                .limit(5)
                .build();
        List<Ticket> tickets = ticketService.getLatest(plr);
        assertEquals("Limit should work as expected and there should be 5 ticket", 5, tickets.size());
        for (Ticket ticket : tickets) {
            log.info(format("Found %s", ticket.toString()));
            assertTrue(ticket.getShow_to_affiliates());
            assertEquals(ticket.getOrigin(), "LED");
            assertEquals(ticket.getDestination(), "MOW");
            assertNotNull(ticket.getDepart_date());
            assertNotNull(ticket.getReturn_date());
            assertNotNull(ticket.getNumber_of_changes());
            assertNotNull(ticket.getValue());
            assertNotNull(ticket.getFound_at());
            assertNotNull(ticket.getDistance());
            assertTrue(ticket.getActual());
        }
    }

    @Test
    public void differentSortingRequestShouldBeImplemented() throws TicketServiceException {
        LatestRequest priceSorting = LatestRequest.builder()
                .origin("LED")
                .destination("DME")
                .sorting(PRICE)
                .show_to_affiliates(false)
                .limit(5)
                .build();
        List<Ticket> byPrice = ticketService.getLatest(priceSorting);
        byPrice.forEach(ticket -> log.info("By price: " + ticket));
        List<Ticket> sortedTickets = byPrice
                .stream()
                .sorted(Comparator.comparing(Ticket::getValue))
                .collect(Collectors.toList());

        assertEquals("After sorting we're expecting that order remain intact. It means that list was already sorted.", byPrice, sortedTickets);
        assertThat(byPrice, hasSize(5));

        LatestRequest routeSorting = LatestRequest.builder()
                .origin("LED")
                .sorting(Sorting.ROUTE)
                .show_to_affiliates(false)
                .limit(5)
                .build();
        List<Ticket> byRoute = ticketService.getLatest(routeSorting);
        assertThat(byRoute, hasSize(5));
        byRoute.forEach(ticket -> log.info("By route: " + ticket));

        LatestRequest distanceUnitSorting = LatestRequest.builder()
                .origin("LED")
                .sorting(Sorting.DISTANCE_UNIT_PRICE)
                .show_to_affiliates(false)
                .limit(5)
                .build();

        List<Ticket> byDistanceUnit = ticketService.getLatest(distanceUnitSorting);
        assertThat(byDistanceUnit, hasSize(5));
        byDistanceUnit.forEach(ticket -> log.info("By distance unit: " + ticket));

    }

    @Test
    public void cheapestPriceForNextYearShouldBeFound() throws TicketServiceException {
        Calendar calendar = Calendar.getInstance();
        Date currentDate = calendar.getTime();
        calendar.setTime(currentDate);


        List<LatestRequest> requests = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            calendar.add(Calendar.MONTH, 1);
            Date nextMonth = calendar.getTime();
            LocalDate localDate = nextMonth.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            int year = localDate.getYear();
            int month = localDate.getMonthValue();
            int firstDayOfMonth = 1;

            LatestRequest request = LatestRequest.builder()
                    .origin("LED")
                    .destination("DME")
                    .sorting(PRICE)
                    .show_to_affiliates(false)
                    .period_type("month")
                    .beginning_of_period(format("%d-%d-%d", year, month, firstDayOfMonth))
                    .limit(1)
                    .build();

            requests.add(request);
        }


        List<Ticket> responses = new ArrayList<>();
        for (LatestRequest request : requests) {
            List<Ticket> latest = ticketService.getLatest(request);
            responses.addAll(latest);
        }

        Ticket cheapestTicket = responses
                .stream()
                .min(Comparator.comparing(Ticket::getValue))
                .orElseThrow();

        log.info("Cheapest price for a next year from LED to DME is " + cheapestTicket.getValue());
        log.info("Cheapest departure date is " + cheapestTicket.getDepart_date());
    }

    @Test
    public void cheapestOneWayTicketFromDMEToAnywhereShouldBeFound() throws TicketServiceException {
        LatestRequest request = LatestRequest.builder()
                .origin("DME")
                .sorting(PRICE)
                .show_to_affiliates(false)
                .limit(5)
                .one_way(true)
                .build();

        List<Ticket> tickets = ticketService.getLatest(request);

        tickets.forEach(ticket -> log.info("Found ticket: " + ticket));
        Ticket cheapestTicket = tickets
                .stream()
                .min(Comparator.comparing(Ticket::getValue))
                .orElseThrow();

        log.info("Cheapest DME one-way ticket destination is " + cheapestTicket.getDestination());
        log.info("Price is " + cheapestTicket.getValue());
        log.info("Departure date is " + cheapestTicket.getDepart_date());
    }

    @Test
    public void cheapestReturnTicketFromDMEToAnywhereShouldBeFound() throws TicketServiceException {
        LatestRequest request = LatestRequest.builder()
                .origin("DME")
                .sorting(PRICE)
                .show_to_affiliates(false)
                .limit(5)
                .one_way(false)
                .build();

        List<Ticket> tickets = ticketService.getLatest(request);

        tickets.forEach(ticket -> log.info("Found ticket: " + ticket));
        Ticket cheapestTicket = tickets
                .stream()
                .min(Comparator.comparing(Ticket::getValue))
                .orElseThrow();

        log.info("Cheapest DME return ticket destination is " + cheapestTicket.getDestination());
        log.info("Price is " + cheapestTicket.getValue());
        log.info("Departure date is " + cheapestTicket.getDepart_date());
    }
}
