package com.example.tickets;

import com.example.tickets.latestprices.LatestPricesGetRequest;
import com.example.tickets.latestprices.LatestPricesGetResponse;
import lombok.extern.java.Log;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import static org.junit.Assert.assertEquals;

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
        assertEquals(plrAsString, "http://api.travelpayouts.com/v2/latestprices/latest?currency=RUB&origin=LED&destination=DME&beginning_of_period=2018-05-01&period_type=month&one_way=false&page=1&limit=30&show_to_affiliates=true&sorting=price&trip_duration=1");
    }


    @Test
    public void latestPricesResponseShouldBeReceived() {
        LatestPricesGetRequest plr = LatestPricesGetRequest.builder()
                .origin("LED")
                .destination("DME")
                .sorting("price")
                .build();
        String plrAsString = plr.toString();
        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory(
                HttpClientBuilder.create().build());
        RestTemplate restTemplate = new RestTemplate(clientHttpRequestFactory);
//        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept-Encoding", "gzip, deflate");
        headers.add("X-Access-Token", "a3bb9c6a5686a67be8be75805a1c4622");
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);


        ResponseEntity<LatestPricesGetResponse> exchange = restTemplate.exchange(plrAsString, HttpMethod.GET, entity, LatestPricesGetResponse.class);
        LatestPricesGetResponse body = exchange.getBody();
        int debug = 0;
    }
}
