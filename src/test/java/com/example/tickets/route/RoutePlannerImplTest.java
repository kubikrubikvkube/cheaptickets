package com.example.tickets.route;

import com.example.tickets.subscription.Subscription;
import com.example.tickets.ticket.CheapTicket;
import com.example.tickets.ticket.CheapTicketService;
import com.example.tickets.travelpayouts.AffilateLinkGenerator;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class RoutePlannerImplTest {
    private final String origin = "LED";
    private final String destination = "MOW";
    private final Integer ticketValue = 1000;
    private final Integer tripDuration = 3;
    private final LocalDate departDate = LocalDate.now();
    private final LocalDate returnDate = LocalDate.now().plusDays(tripDuration);

    @Autowired
    private AffilateLinkGenerator affilateLinkGenerator;

    @Test
    @Disabled
    void shouldFindRoutesByOriginDestinationAndTripDuration() {
        Subscription subscription = new Subscription();
        subscription.setOrigin(origin);
        subscription.setDestination(destination);
        subscription.setTripDurationInDaysFrom(tripDuration);
        subscription.setTripDurationInDaysTo(tripDuration);

        CheapTicket departTicket = new CheapTicket();
        departTicket.setOrigin(origin);
        departTicket.setDestination(destination);
        departTicket.setDepartDate(departDate);
        departTicket.setValue(ticketValue);

        CheapTicket cheapReturnTicket = new CheapTicket();
        cheapReturnTicket.setOrigin(destination);
        cheapReturnTicket.setDestination(origin);
        cheapReturnTicket.setDepartDate(returnDate);
        cheapReturnTicket.setValue(500);

        CheapTicket expensiveReturnTicket = new CheapTicket();
        expensiveReturnTicket.setOrigin(destination);
        expensiveReturnTicket.setDestination(origin);
        expensiveReturnTicket.setDepartDate(returnDate);
        expensiveReturnTicket.setValue(3000);

        CheapTicketService cheapTicketService = Mockito.mock(CheapTicketService.class);
        when(cheapTicketService.findByOriginAndDestination(origin, destination)).thenReturn(List.of(departTicket));
        when(cheapTicketService.findByOriginAndDestinationAndDepartDate(origin, destination, returnDate)).thenReturn(List.of(cheapReturnTicket, expensiveReturnTicket));

        RoutePlanner routePlanner = new RoutePlannerImpl(cheapTicketService, affilateLinkGenerator);
        List<RouteDto> routesList = routePlanner.plan(subscription);
        assertThat(routesList, hasSize(1));
        RouteDto routeDto = routesList.get(0);
        assertEquals(origin, routeDto.getOrigin());
        assertEquals(destination, routeDto.getDestination());
        assertEquals(departTicket, routeDto.getDepartTicket());
        assertEquals(cheapReturnTicket, routeDto.getReturnTicket());
        Integer expectedSum = departTicket.getValue() + cheapReturnTicket.getValue();
        assertEquals(expectedSum, routeDto.getSumValue());
        assertEquals(tripDuration, routeDto.getTripDurationInDays());
    }

    @Test
    @Disabled
    void shouldFindRoutesByOriginDestinationAndDepartDate() {
        Subscription subscription = new Subscription();
        subscription.setOrigin(origin);
        subscription.setDestination(destination);
        subscription.setDepartDate(departDate);

        CheapTicket departTicket = new CheapTicket();
        departTicket.setOrigin(origin);
        departTicket.setDestination(destination);
        departTicket.setDepartDate(departDate);
        departTicket.setValue(ticketValue);

        CheapTicketService cheapTicketService = Mockito.mock(CheapTicketService.class);
        when(cheapTicketService.findByOriginAndDestination(origin, destination)).thenReturn(List.of(departTicket));

        RoutePlanner routePlanner = new RoutePlannerImpl(cheapTicketService, affilateLinkGenerator);
        List<RouteDto> routesList = routePlanner.plan(subscription);
        assertThat(routesList, hasSize(1));
        RouteDto routeDto = routesList.get(0);
        assertEquals(origin, routeDto.getOrigin());
        assertEquals(destination, routeDto.getDestination());
        assertEquals(departTicket, routeDto.getDepartTicket());
        assertEquals(ticketValue, routeDto.getSumValue());
    }

    @Test
    @Disabled
    void shouldFindRoutesByOriginDestination() {
        Subscription subscription = new Subscription();
        subscription.setOrigin(origin);
        subscription.setDestination(destination);

        CheapTicket cheapDepartTicket = new CheapTicket();
        cheapDepartTicket.setOrigin(origin);
        cheapDepartTicket.setDestination(destination);
        cheapDepartTicket.setDepartDate(departDate);
        cheapDepartTicket.setValue(ticketValue);

        CheapTicket expensiveDepartTicket = new CheapTicket();
        expensiveDepartTicket.setOrigin(origin);
        expensiveDepartTicket.setDestination(destination);
        expensiveDepartTicket.setDepartDate(LocalDate.now());
        Integer expensiveTicketValue = ticketValue + 1000;
        expensiveDepartTicket.setValue(expensiveTicketValue);

        CheapTicketService cheapTicketService = Mockito.mock(CheapTicketService.class);
        when(cheapTicketService.findByOriginAndDestination(origin, destination)).thenReturn(List.of(cheapDepartTicket, expensiveDepartTicket));

        RoutePlanner routePlanner = new RoutePlannerImpl(cheapTicketService, affilateLinkGenerator);
        List<RouteDto> routesList = routePlanner.plan(subscription);
        assertThat(routesList, hasSize(2));
        RouteDto cheapRouteDto = routesList.get(0);
        RouteDto expensiveRouteDto = routesList.get(1);
        assertEquals(origin, cheapRouteDto.getOrigin());
        assertEquals(destination, cheapRouteDto.getDestination());
        assertEquals(origin, expensiveRouteDto.getOrigin());
        assertEquals(destination, expensiveRouteDto.getDestination());
        assertEquals(cheapDepartTicket, cheapRouteDto.getDepartTicket());
        assertEquals(expensiveDepartTicket, expensiveRouteDto.getDepartTicket());
        assertEquals(ticketValue, cheapDepartTicket.getValue());
        assertEquals(expensiveTicketValue, expensiveDepartTicket.getValue());
    }

    @Test
    @Disabled
    void shouldFindRoutesByOriginDestinationAndDepartDateAndReturnDate() {
        Subscription subscription = new Subscription();
        subscription.setOrigin(origin);
        subscription.setDestination(destination);
        subscription.setDepartDate(departDate);
        subscription.setReturnDate(returnDate);

        CheapTicket cheapDepartTicket = new CheapTicket();
        cheapDepartTicket.setOrigin(origin);
        cheapDepartTicket.setDestination(destination);
        cheapDepartTicket.setDepartDate(departDate);
        cheapDepartTicket.setValue(ticketValue);

        CheapTicket cheapReturnTicket = new CheapTicket();
        cheapReturnTicket.setOrigin(destination);
        cheapReturnTicket.setDestination(origin);
        cheapReturnTicket.setDepartDate(returnDate);
        cheapReturnTicket.setValue(ticketValue);

        CheapTicket expensiveDepartTicket = new CheapTicket();
        expensiveDepartTicket.setOrigin(origin);
        expensiveDepartTicket.setDestination(destination);
        expensiveDepartTicket.setDepartDate(departDate);
        expensiveDepartTicket.setValue(ticketValue * 2);

        CheapTicket expensiveReturnTicket = new CheapTicket();
        expensiveReturnTicket.setOrigin(destination);
        expensiveReturnTicket.setDestination(origin);
        expensiveReturnTicket.setDepartDate(returnDate);
        expensiveReturnTicket.setValue(ticketValue * 2);


        CheapTicketService cheapTicketService = Mockito.mock(CheapTicketService.class);
        when(cheapTicketService.findByOriginAndDestinationAndDepartDate(origin, destination, departDate)).thenReturn(List.of(cheapDepartTicket, expensiveDepartTicket));
        when(cheapTicketService.findByOriginAndDestinationAndDepartDate(destination, origin, returnDate)).thenReturn(List.of(cheapReturnTicket, expensiveReturnTicket));

        RoutePlanner routePlanner = new RoutePlannerImpl(cheapTicketService, affilateLinkGenerator);
        List<RouteDto> routesList = routePlanner.plan(subscription);
        assertThat(routesList, hasSize(4));
        RouteDto cheapestRouteDto = routesList.get(0);
        assertEquals(origin, cheapestRouteDto.getOrigin());
        assertEquals(destination, cheapestRouteDto.getDestination());
        assertEquals(cheapDepartTicket, cheapestRouteDto.getDepartTicket());
        assertEquals(cheapReturnTicket, cheapestRouteDto.getReturnTicket());
        assertEquals(Integer.valueOf(ticketValue + ticketValue), cheapestRouteDto.getSumValue());
        var expectedTripDuration = Integer.valueOf(departDate.until(returnDate).getDays());
        assertEquals(expectedTripDuration, cheapestRouteDto.getTripDurationInDays());

    }
}