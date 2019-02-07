package com.example.tickets.travelpayouts;

import com.example.tickets.route.RouteDto;
import com.example.tickets.ticket.Ticket;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@PropertySource("classpath:ticket.properties")
class AffilateLinkGeneratorImplTest {
    @Autowired
    private AffilateLinkGenerator linkGenerator;

    @Value("${travelpayouts.marker}")
    private String marker;

    @Test
    void generateFromDTO() {
        LocalDate date = LocalDate.of(2019, 1, 1);
        Ticket departTicket = new Ticket();
        departTicket.setDepartDate(date);

        Ticket returnTicket = new Ticket();
        returnTicket.setDepartDate(date);

        RouteDto routeDto = new RouteDto();
        routeDto.setOrigin("LED");
        routeDto.setDestination("MOW");
        routeDto.setDepartTicket(departTicket);
        routeDto.setReturnTicket(returnTicket);

        var expectedString = "https://www.aviasales.ru/search?origin_iata=LED&destination_iata=MOW&departDate=2019-01-01&returnDate=2019-01-01&with_request=true&marker=12345";
        String generatedString = linkGenerator.generate(routeDto);
        assertEquals(expectedString, generatedString);
    }
}