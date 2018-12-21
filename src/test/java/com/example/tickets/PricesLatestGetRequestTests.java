package com.example.tickets;

import com.example.tickets.prices.PricesLatestGetRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.java.Log;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@Log
public class PricesLatestGetRequestTests {

    @Test
    public void pricesLatestRequestShouldBeCreated() throws JsonProcessingException {
        PricesLatestGetRequest plr = PricesLatestGetRequest.builder()
                .origin("LED")
                .one_way(true)
                .destination("DME")
                .build();
        String plrAsString = plr.toString();
        log.info(plrAsString);
    }
}
