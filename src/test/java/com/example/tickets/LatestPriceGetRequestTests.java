package com.example.tickets;

import com.example.tickets.httpclient.DefaultHttpClient;
import com.example.tickets.latestprices.LatestPriceGetRequest;
import com.example.tickets.latestprices.LatestPriceGetResponse;
import com.example.tickets.latestprices.Ticket;
import lombok.extern.java.Log;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

import static java.lang.String.format;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Log
public class LatestPriceGetRequestTests {
    @Autowired
    DefaultHttpClient defaultHttpClient;

    @Test
    public void latestPricesRequestShouldBeBuilded() {
        LatestPriceGetRequest plr = LatestPriceGetRequest.builder()
                .currency("RUB")
                .origin("LED")
                .destination("DME")
                .beginning_of_period("2018-05-01")
                .period_type("month")
                .one_way(false)
                .page(1)
                .limit(30)
                .show_to_affiliates(true)
                .sorting("price")
                .trip_duration(1)
                .build();
        String plrAsString = plr.toString();
        log.info("LatestPriceGetRequest created:" + plrAsString);
        assertEquals(plr.getCurrency(), "RUB");
        assertEquals(plr.getOrigin(), "LED");
        assertEquals(plr.getDestination(), "DME");
        assertEquals(plr.getBeginning_of_period(), "2018-05-01");
        assertEquals(plr.getPeriod_type(), "month");
        assertEquals(plr.getOne_way(), false);
        assertEquals(plr.getPage(), Integer.valueOf(1));
        assertEquals(plr.getLimit(), Integer.valueOf(30));
        assertEquals(plr.getShow_to_affiliates(), true);
        assertEquals(plr.getSorting(), "price");
        assertEquals(plr.getTrip_duration(), Integer.valueOf(1));
        assertEquals("http://api.travelpayouts.com/v2/prices/latest?currency=RUB&origin=LED&destination=DME&beginning_of_period=2018-05-01&period_type=month&one_way=false&page=1&limit=30&show_to_affiliates=true&sorting=price&trip_duration=1", plrAsString);
    }


    @Test
    public void latestPricesResponseShouldBeReceived() {
        LatestPriceGetRequest plr = LatestPriceGetRequest.builder()
                .origin("LED")
                .destination("DME")
                .sorting("price")
                .show_to_affiliates(true)
                .limit(5)
                .build();
        ResponseEntity<LatestPriceGetResponse> wrappedResponse = defaultHttpClient.get(plr, LatestPriceGetResponse.class);
        LatestPriceGetResponse response = wrappedResponse.getBody();
        assertNotNull(response);
        assertNotNull(response.getSuccess());
        assertNotNull(response.getData());
        assertTrue("Response should be successfull", response.getSuccess());
        List<Ticket> tickets = response.getData();
        assertEquals("Limit should work as expected and there should be 5 tickets", 5, tickets.size());
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
    public void cheapestPriceForNextYearShouldBeFound() {
        Calendar calendar = Calendar.getInstance();
        Date currentDate = calendar.getTime();
        calendar.setTime(currentDate);


        List<LatestPriceGetRequest> requests = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            calendar.add(Calendar.MONTH, 1);
            Date nextMonth = calendar.getTime();
            LocalDate localDate = nextMonth.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            int year = localDate.getYear();
            int month = localDate.getMonthValue();
            int firstDayOfMonth = 1;

            LatestPriceGetRequest request = LatestPriceGetRequest.builder()
                    .origin("LED")
                    .destination("DME")
                    .sorting("price")
                    .show_to_affiliates(false)
                    .period_type("month")
                    .beginning_of_period(format("%d-%d-%d", year, month, firstDayOfMonth))
                    .limit(1)
                    .build();

            requests.add(request);
        }
        List<LatestPriceGetResponse> responses = new ArrayList<>();
        for (LatestPriceGetRequest request : requests) {
            ResponseEntity<LatestPriceGetResponse> wrappedResponse = defaultHttpClient.get(request, LatestPriceGetResponse.class);
            LatestPriceGetResponse response = wrappedResponse.getBody();
            assertNotNull(response);
            assertNotNull(response.getSuccess());
            assertNotNull(response.getData());
            responses.add(response);
        }

        Ticket cheapestTicket = responses
                .stream()
                .map(LatestPriceGetResponse::getData)
                .flatMap(List::stream)
                .min(Comparator.comparing(Ticket::getValue))
                .orElseThrow();

        log.info("Cheapest price for a next year from LED to DME is " + cheapestTicket.getValue());
        log.info("Cheapest departure date is " + cheapestTicket.getDepart_date());
    }
}
