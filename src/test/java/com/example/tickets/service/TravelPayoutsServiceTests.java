package com.example.tickets.service;

import com.example.tickets.ticket.Ticket;
import com.example.tickets.travelpayouts.TravelPayoutsService;
import com.example.tickets.travelpayouts.request.DirectRequest;
import com.example.tickets.travelpayouts.request.LatestRequest;
import com.example.tickets.travelpayouts.request.Sorting;
import com.example.tickets.util.ServiceException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

import static com.example.tickets.travelpayouts.request.Sorting.PRICE;
import static java.lang.String.format;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TravelPayoutsServiceTests {
    private final Logger log = LoggerFactory.getLogger(TravelPayoutsServiceTests.class);
    @Autowired
    private TravelPayoutsService travelPayoutsService;

    @Test
    public void direct() throws IOException {
        DirectRequest request = DirectRequest
                .builder()
                .origin("LED")
                .destination("MOW")
                .depart_date(LocalDate.now().plusDays(14))
                .build();

        List<Ticket> directTickets = travelPayoutsService.getDirect(request);
        log.info("Found direct tickets: " + directTickets);
        assertNotNull(directTickets);
        assertThat(directTickets, hasSize(greaterThanOrEqualTo(1)));
    }

    @Test
    public void latestPricesRequestShouldBeBuilded() {
        var firstJanuary = LocalDate.of(2019, 1, 1);
        LatestRequest plr = LatestRequest.builder()
                .currency("RUB")
                .origin("LED")
                .destination("DME")
                .beginning_of_period(firstJanuary)
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
        assertEquals(plr.getBeginning_of_period(), firstJanuary);
        assertEquals(plr.getPeriod_type(), "month");
        assertEquals(plr.getOne_way(), false);
        assertEquals(plr.getPage(), Integer.valueOf(1));
        assertEquals(plr.getLimit(), Integer.valueOf(5));
        assertEquals(plr.getShow_to_affiliates(), true);
        assertEquals(plr.getSorting(), PRICE);
        assertEquals(plr.getTrip_duration(), Integer.valueOf(1));
    }


    @Test
    public void latestPricesResponseShouldBeReceived() throws ServiceException {
        LatestRequest plr = LatestRequest.builder()
                .origin("LED")
                .destination("DME")
                .sorting(PRICE)
                .show_to_affiliates(true)
                .limit(5)
                .build();
        List<Ticket> tickets = travelPayoutsService.getLatest(plr);
        assertEquals("Limit should work as expected and there should be 5 ticket", 5, tickets.size());
        for (Ticket ticket : tickets) {
            log.info(format("Found %s", ticket.toString()));
            assertTrue(ticket.getShowToAffiliates());
            assertEquals(ticket.getOrigin(), "LED");
            assertEquals(ticket.getDestination(), "MOW");
            assertNotNull(ticket.getDepartDate());
            assertNotNull(ticket.getDepartTime());
            assertNotNull(ticket.getNumberOfChanges());
            assertNotNull(ticket.getValue());
            assertNotNull(ticket.getFoundAt());
            assertNotNull(ticket.getDistance());
            assertTrue(ticket.getActual());
        }
    }

    @Test
    public void differentSortingRequestShouldBeImplemented() throws ServiceException {
        LatestRequest priceSorting = LatestRequest.builder()
                .origin("LED")
                .destination("DME")
                .sorting(PRICE)
                .show_to_affiliates(false)
                .limit(5)
                .build();
        List<Ticket> byPrice = travelPayoutsService.getLatest(priceSorting);
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
        List<Ticket> byRoute = travelPayoutsService.getLatest(routeSorting);
        assertThat(byRoute, hasSize(5));
        byRoute.forEach(ticket -> log.info("By route: " + ticket));

        LatestRequest distanceUnitSorting = LatestRequest.builder()
                .origin("LED")
                .sorting(Sorting.DISTANCE_UNIT_PRICE)
                .show_to_affiliates(false)
                .limit(5)
                .build();

        List<Ticket> byDistanceUnit = travelPayoutsService.getLatest(distanceUnitSorting);
        assertThat(byDistanceUnit, hasSize(5));
        byDistanceUnit.forEach(ticket -> log.info("By distance unit: " + ticket));

    }

    @Test
    public void cheapestPriceForNextYearShouldBeFound() throws ServiceException {
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
                    .beginning_of_period(LocalDate.of(year, month, firstDayOfMonth))
                    .limit(1)
                    .build();

            requests.add(request);
        }


        List<Ticket> responses = new ArrayList<>();
        for (LatestRequest request : requests) {
            List<Ticket> latest = travelPayoutsService.getLatest(request);
            responses.addAll(latest);
        }

        Ticket cheapestTicket = responses
                .stream()
                .min(Comparator.comparing(Ticket::getValue))
                .orElseThrow();

        log.info("Cheapest price for a next year from LED to DME is " + cheapestTicket.getValue());
        log.info("Cheapest departure date is " + cheapestTicket.getDepartDate());
    }

    @Test
    public void cheapestOneWayTicketFromDMEToAnywhereShouldBeFound() throws ServiceException {
        LatestRequest request = LatestRequest.builder()
                .origin("DME")
                .sorting(PRICE)
                .show_to_affiliates(false)
                .limit(5)
                .one_way(true)
                .build();

        List<Ticket> tickets = travelPayoutsService.getLatest(request);

        tickets.forEach(ticket -> log.info("Found ticket: " + ticket));
        Ticket cheapestTicket = tickets
                .stream()
                .min(Comparator.comparing(Ticket::getValue))
                .orElseThrow();

        log.info("Cheapest DME one-way ticket destination is " + cheapestTicket.getDestination());
        log.info("Price is " + cheapestTicket.getValue());
        log.info("Departure date is " + cheapestTicket.getDepartDate());
    }

    @Test
    public void cheapestReturnTicketFromDMEToAnywhereShouldBeFound() throws ServiceException {
        LatestRequest request = LatestRequest.builder()
                .origin("DME")
                .sorting(PRICE)
                .show_to_affiliates(false)
                .limit(5)
                .one_way(false)
                .build();

        List<Ticket> tickets = travelPayoutsService.getLatest(request);

        tickets.forEach(ticket -> log.info("Found ticket: " + ticket));
        Ticket cheapestTicket = tickets
                .stream()
                .min(Comparator.comparing(Ticket::getValue))
                .orElseThrow();

        log.info("Cheapest DME return ticket destination is " + cheapestTicket.getDestination());
        log.info("Price is " + cheapestTicket.getValue());
        log.info("Departure date is " + cheapestTicket.getDepartDate());
    }
}
