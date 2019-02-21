package com.example.tickets.iata;

import com.example.tickets.util.DefaultHttpClient;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class IataServiceImplTest {

    @Autowired
    private IataService service;

    @Test
    void fromCode() {
        Iata iata = service.fromCode("BER");
        assertNotNull(iata);
        assertEquals("Берлин", iata.getPlace());
        assertEquals("BER", iata.getCode());
        assertNotNull(iata.getId());
    }

    @Test
    void resolve() throws IOException {
        var request = "https://places.aviasales.ru/v2/places.json?locale=ru&max=1&term=масква&types[]=city&types[]=airport&types[]=country";
        var response = "[{\"weight\":1006321,\"index_strings\":[\"defaultcity\",\"defaultcity\",\"maskava\",\"maskva\",\"mosca\",\"moscou\",\"moscova\",\"moscovo\",\"moscow\",\"moscú\",\"moskau\",\"moskou\",\"moskova\",\"moskow\",\"moskva\",\"moskwa\",\"moszkva\",\"μόσχα\",\"москва\",\"нерезиновая\",\"нерезиновая\",\"нерезиновск\",\"нерезиновск\",\"понаехавск\",\"понаехавск\",\"մոսկվա\",\"מוסקבה\",\"مسکو\",\"موسكو\",\"मास्को\",\"มอสโก\",\"მოსკოვი\",\"モスクワ\",\"莫斯科\",\"모스크바\"],\"code\":\"MOW\",\"cases\":{\"pr\":\"Москве\",\"da\":\"Москве\",\"tv\":\"Москвой\",\"vi\":\"в Москву\",\"ro\":\"Москвы\"},\"state_code\":null,\"country_code\":\"RU\",\"place\":\"Москва\",\"coordinates\":{\"lon\":37.617633,\"lat\":55.755786},\"country_name\":\"Россия\",\"main_airport_name\":null,\"type\":\"city\",\"country_cases\":null}]";
        DefaultHttpClient mock = Mockito.mock(DefaultHttpClient.class);
        JsonNode jsonNode = new ObjectMapper().readValue(response, JsonNode.class);
        when(mock.getJsonResponseWithoutHeaders(request)).thenReturn(CompletableFuture.completedFuture(jsonNode));

        var expectedResponse = "MOW";
        Iata result = service.resolve("масква");
        assertEquals(expectedResponse, result.getCode());
    }
}