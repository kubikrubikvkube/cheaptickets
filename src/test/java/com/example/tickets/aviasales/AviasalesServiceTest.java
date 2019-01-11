package com.example.tickets.aviasales;

import com.example.tickets.AppConfig;
import com.example.tickets.aviasales.response.AviasalesResponse;
import com.example.tickets.ticket.Ticket;
import com.example.tickets.util.DefaultHttpClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.withSettings;

@ExtendWith(SpringExtension.class)
class AviasalesServiceTest {
    private static AviasalesService aviasalesService;

    @BeforeAll
    static void setup() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        ModelMapper modelMapper = new AppConfig().defaultMapper();
        ArrayNode root = objectMapper.createArrayNode();
        ObjectNode tickets = objectMapper.readValue("{\n" +
                "   \"value\":28912.0,\n" +
                "   \"ttl\":1547130970,\n" +
                "   \"trip_class\":0,\n" +
                "   \"show_to_affiliates\":false,\n" +
                "   \"return_date\":\"2019-05-19\",\n" +
                "   \"origin\":\"LED\",\n" +
                "   \"number_of_changes\":1,\n" +
                "   \"gate\":\"Trip.ru\",\n" +
                "   \"found_at\":\"2019-01-08T14:36:10\",\n" +
                "   \"distance\":0,\n" +
                "   \"destination\":\"SYZ\",\n" +
                "   \"depart_date\":\"2019-05-17\",\n" +
                "   \"created_at\":1546958170,\n" +
                "   \"actual\":true\n" +
                "}", ObjectNode.class);
        root.add(tickets);
        DefaultHttpClient<AviasalesResponse> defaultHttpClient = Mockito.mock(DefaultHttpClient.class, withSettings().verboseLogging());
        when(defaultHttpClient.getJsonResponseWithoutHeaders("https://lyssa.aviasales.ru/map?origin_iata=LED&one_way=false&min_trip_duration=1&max_trip_duration=3&show_to_affiliates=false")).thenReturn(root);

        aviasalesService = new AviasalesServiceImpl(defaultHttpClient, modelMapper);
    }

    @Test
    void getTicketsMap_originIAT_minTripDuration_maxTripDuration() {
        var originIATA = "LED";
        var minTripDuration = 1;
        var maxTripDuration = 3;
        List<Ticket> ticketsMap = aviasalesService.getTicketsMap(originIATA, minTripDuration, maxTripDuration);
        assertThat(ticketsMap, hasSize(1));
        Ticket t = ticketsMap.get(0);
        assertNotNull(t);
        assertThat(t.getValue(), equalTo(28912));
        assertThat(t.getTtl(), equalTo(1547130970L));
        assertThat(t.getTripClass(), equalTo(0));
        assertFalse(t.getShowToAffiliates());
        assertThat(t.getReturnDate(), equalTo(LocalDate.parse("2019-05-19")));
        assertThat(t.getOrigin(), equalTo("LED"));
        assertThat(t.getNumberOfChanges(), equalTo(1));
        assertThat(t.getGate(), equalTo("Trip.ru"));
        assertThat(t.getFoundAt(), equalTo(LocalDateTime.parse("2019-01-08T14:36:10")));
        assertThat(t.getDistance(), equalTo(0));
        assertThat(t.getDestination(), equalTo("SYZ"));
        assertThat(t.getDepartDate(), equalTo(LocalDate.parse("2019-05-17")));
        assertThat(t.getCreatedAt(), equalTo(1546958170));
        assertTrue(t.getActual());
    }

}