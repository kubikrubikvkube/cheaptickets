package com.example.tickets;

import com.example.tickets.prices.PricesLatestGetRequest;
import lombok.extern.java.Log;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@Log
public class PricesLatestGetRequestTests {

    @Test
    public void pricesLatestRequestShouldBeBuilded() {
        PricesLatestGetRequest plr = PricesLatestGetRequest.builder()
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
        log.info("PricesLatestGetRequest created:" + plrAsString);
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
        assertEquals(plrAsString, "http://api.travelpayouts.com/v2/prices/latest?currency=RUB&origin=LED&destination=DME&beginning_of_period=2018-05-01&period_type=month&one_way=false&page=1&limit=30&show_to_affiliates=true&sorting=price&trip_duration=1");
    }
}
