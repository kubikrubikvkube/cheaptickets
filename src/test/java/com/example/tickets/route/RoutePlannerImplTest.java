package com.example.tickets.route;

import com.example.tickets.subscription.Subscription;
import com.example.tickets.ticket.CheapTicket;
import com.example.tickets.ticket.CheapTicketService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class RoutePlannerImplTest {
    private String origin = "LED";
    private String destination = "MOW";
    private Integer ticketValue = 1000;
    private Integer tripDuration = 3;
    private LocalDate departDate = LocalDate.now();
    private LocalDate returnDate = LocalDate.now().plusDays(tripDuration);

    @Test
    void shouldFindRoutesByOriginDestinationAndTripDuration() {
        Subscription subscription = new Subscription();
        subscription.setOrigin(origin);
        subscription.setDestination(destination);
        subscription.setTripDurationInDays(tripDuration);

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

        RoutePlanner routePlanner = new RoutePlannerImpl(cheapTicketService);
        List<RouteDTO> routesList = routePlanner.plan(subscription);
        assertThat(routesList, hasSize(1));
        RouteDTO routeDTO = routesList.get(0);
        assertEquals(origin, routeDTO.getOrigin());
        assertEquals(destination, routeDTO.getDestination());
        assertEquals(departTicket, routeDTO.getDepartTicket());
        assertEquals(cheapReturnTicket, routeDTO.getReturnTicket());
        Integer expectedSum = departTicket.getValue() + cheapReturnTicket.getValue();
        assertEquals(expectedSum, routeDTO.getSumValue());
        assertEquals(tripDuration, routeDTO.getTripDurationInDays());
        assertNull(routeDTO.getTicketNotifications());
    }

    @Test
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

        RoutePlanner routePlanner = new RoutePlannerImpl(cheapTicketService);
        List<RouteDTO> routesList = routePlanner.plan(subscription);
        assertThat(routesList, hasSize(1));
        RouteDTO routeDTO = routesList.get(0);
        assertEquals(origin, routeDTO.getOrigin());
        assertEquals(destination, routeDTO.getDestination());
        assertEquals(departTicket, routeDTO.getDepartTicket());
        assertEquals(ticketValue, routeDTO.getSumValue());
        assertNull(routeDTO.getTicketNotifications());
    }

    @Test
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

        RoutePlanner routePlanner = new RoutePlannerImpl(cheapTicketService);
        List<RouteDTO> routesList = routePlanner.plan(subscription);
        assertThat(routesList, hasSize(2));
        RouteDTO cheapRouteDTO = routesList.get(0);
        RouteDTO expensiveRouteDTO = routesList.get(1);
        assertEquals(origin, cheapRouteDTO.getOrigin());
        assertEquals(destination, cheapRouteDTO.getDestination());
        assertEquals(origin, expensiveRouteDTO.getOrigin());
        assertEquals(destination, expensiveRouteDTO.getDestination());
        assertEquals(cheapDepartTicket, cheapRouteDTO.getDepartTicket());
        assertEquals(expensiveDepartTicket, expensiveRouteDTO.getDepartTicket());
        assertEquals(ticketValue, cheapDepartTicket.getValue());
        assertEquals(expensiveTicketValue, expensiveDepartTicket.getValue());
        assertNull(cheapRouteDTO.getTicketNotifications());
        assertNull(expensiveRouteDTO.getTicketNotifications());
    }

    @Test
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

        RoutePlanner routePlanner = new RoutePlannerImpl(cheapTicketService);
        List<RouteDTO> routesList = routePlanner.plan(subscription);
        assertThat(routesList, hasSize(4));
        RouteDTO cheapestRouteDTO = routesList.get(0);
        assertEquals(origin, cheapestRouteDTO.getOrigin());
        assertEquals(destination, cheapestRouteDTO.getDestination());
        assertEquals(cheapDepartTicket, cheapestRouteDTO.getDepartTicket());
        assertEquals(cheapReturnTicket, cheapestRouteDTO.getReturnTicket());
        assertEquals(Integer.valueOf(ticketValue + ticketValue), cheapestRouteDTO.getSumValue());
        assertNull(cheapestRouteDTO.getTicketNotifications());
        var expectedTripDuration = Integer.valueOf(departDate.until(returnDate).getDays());
        assertEquals(expectedTripDuration, cheapestRouteDTO.getTripDurationInDays());

    }
}