package com.example.tickets.aviasales;


import com.example.tickets.ticket.TicketDTOMapper;
import com.example.tickets.util.DefaultHttpClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.withSettings;

@ExtendWith(SpringExtension.class)
public class AviasalesServiceTest {
    private static AviasalesService aviasalesService;

    @BeforeAll
    static void setup() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        TicketDTOMapper mapper = TicketDTOMapper.INSTANCE;
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

        aviasalesService = new AviasalesServiceImpl(defaultHttpClient);
    }

}