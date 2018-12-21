package com.example.tickets;

import com.example.tickets.httpclient.HttpClient;
import com.example.tickets.latestprices.LatestPricesGetRequest;
import com.example.tickets.latestprices.LatestPricesGetResponse;
import com.example.tickets.latestprices.Ticket;
import lombok.extern.java.Log;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static java.lang.String.format;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Log
public class LatestPricesGetRequestTests {

    @Test
    public void latestPricesRequestShouldBeBuilded() {
        LatestPricesGetRequest plr = LatestPricesGetRequest.builder()
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
        log.info("LatestPricesGetRequest created:" + plrAsString);
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
        LatestPricesGetRequest plr = LatestPricesGetRequest.builder()
                .origin("LED")
                .destination("DME")
                .sorting("price")
                .show_to_affiliates(true)
                .limit(5)
                .build();
        HttpClient httpClient = new HttpClient();
        ResponseEntity<LatestPricesGetResponse> wrappedResponse = httpClient.get(plr, LatestPricesGetResponse.class);
        LatestPricesGetResponse response = wrappedResponse.getBody();
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
}
