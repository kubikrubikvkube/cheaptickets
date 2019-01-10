package com.example.tickets.aviasales;

import com.example.tickets.ticket.Ticket;
import com.example.tickets.util.DefaultHttpClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class AviasalesServiceTest {
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    ObjectMapper objectMapper;

    AviasalesService aviasalesService;

    @BeforeEach
    void setUp() throws IOException {
        ArrayNode root = objectMapper.createArrayNode();
        ObjectNode tickets = objectMapper.readValue("{\"value\":28912.0,\"ttl\":1547130970,\"trip_class\":0,\"show_to_affiliates\":false,\"return_date\":\"2019-05-19\",\"origin\":\"LED\",\"number_of_changes\":1,\"gate\":\"Trip.ru\",\"found_at\":\"2019-01-08T14:36:10\",\"distance\":0,\"destination\":\"SYZ\",\"depart_date\":\"2019-05-17\",\"created_at\":1546958170,\"actual\":true}", ObjectNode.class);
        root.add(tickets);
        DefaultHttpClient defaultHttpClient = Mockito.mock(DefaultHttpClient.class);
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
        assertEquals(originIATA, t.getOrigin());
    }

}