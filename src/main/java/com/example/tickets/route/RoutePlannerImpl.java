package com.example.tickets.route;

import com.example.tickets.subscription.Subscription;
import com.example.tickets.subscription.SubscriptionType;
import com.example.tickets.ticket.CheapTicket;
import com.example.tickets.ticket.CheapTicketService;
import com.example.tickets.ticket.Ticket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.*;

@Component
public class RoutePlannerImpl implements RoutePlanner {
    private final CheapTicketService cheapTicketService;
    private final Logger log = LoggerFactory.getLogger(RoutePlannerImpl.class);
    private final Integer MAXIMUM_REASONABLE_TRIP_TIME = 30;

    public RoutePlannerImpl(CheapTicketService cheapTicketService) {
        this.cheapTicketService = cheapTicketService;
    }

    @Override
    public List<RouteDTO> plan(Subscription subscription) {
        var subscriptionType = subscription.getSubscriptionType();
        if (subscriptionType == null || subscriptionType == SubscriptionType.INVALID) {
            log.debug("SubscriptionType for {} is invalid {}. Skipping subscription", subscription, subscriptionType);
            return Collections.emptyList();
        }
        var origin = subscription.getOrigin();
        var destination = subscription.getDestination();
        var departDate = subscription.getDepartDate();
        var returnDate = subscription.getReturnDate();
        var tripDurationInDaysFrom = subscription.getTripDurationInDaysFrom();
        var tripDurationInDaysTo = subscription.getTripDurationInDaysTo();

        List<RouteDTO> routes = new ArrayList<>();
        switch (subscriptionType) {
            case DESTINATION_TRIP_DURATION_FROM_TRIP_DURATION_TO: {
                routes = planReturnTripWithTripDurationFromTo(origin, destination, tripDurationInDaysFrom, tripDurationInDaysTo);
                break;
            }

            case TRIP_DURATION_FROM_TRIP_DURATION_TO:
                routes = planReturnTripAnywhereWithDepartReturnDate(origin, departDate, returnDate);
                break;

            case DESTINATION:
                routes = planOneWayTrip(origin, destination);
                break;

            case DESTINATION_DEPART_DATE:
                routes = planOneWayTripWithDepartDate(origin, destination, departDate);
                break;

            case DEPART_DATE_AND_RETURN_DATE:
                routes = planReturnTripWithDepartAndReturnDates(departDate, returnDate);
                break;

            case DESTINATION_DEPART_DATE_RETURN_DATE:
                routes = planReturnTripWithDepartAndReturnDates(origin, destination, departDate, returnDate);
                break;

            case DESTINATION_DEPART_DATE_TRIP_DURATION_FROM:
                routes = planReturnTripAnywhereWithDepartDateForSpecificNumberOfDays(origin, destination, departDate, tripDurationInDaysFrom);
                break;

            case DESTINATION_TRIP_DURATION_FROM: {
                routes = planReturnTripWithTripDurationFromTo(origin, destination, tripDurationInDaysFrom, MAXIMUM_REASONABLE_TRIP_TIME);
                break;
            }
            case DESTINATION_TRIP_DURATION_TO:
                routes = planReturnTripWithTripDurationFromTo(origin, destination, 1, tripDurationInDaysTo);
                break;
            case INVALID:
                routes = Collections.emptyList();
                break;
        }

        return routes;
    }

    private List<RouteDTO> planReturnTripWithDepartAndReturnDates(String origin, String destination, LocalDate departDate, LocalDate returnDate) {
        List<CheapTicket> departTickets = cheapTicketService.findByOriginAndDestinationAndDepartDate(origin, destination, departDate);
        List<CheapTicket> returnTickets = cheapTicketService.findByOriginAndDestinationAndDepartDate(destination, origin, returnDate);
        List<CheapTicket> departTicketsMutable = new ArrayList<>(departTickets);
        List<CheapTicket> returnTicketsMutable = new ArrayList<>(returnTickets);
        departTicketsMutable.sort(Comparator.comparingInt(Ticket::getValue));
        returnTicketsMutable.sort(Comparator.comparingInt(Ticket::getValue));

        List<RouteDTO> availableRoutes = new ArrayList<>();
        for (CheapTicket departTicket : departTicketsMutable) {
            for (CheapTicket returnTicket : returnTicketsMutable) {
                RouteDTO routeDTO = new RouteDTO();
                routeDTO.setOrigin(origin);
                routeDTO.setDestination(destination);
                routeDTO.setDepartTicket(departTicket);
                routeDTO.setReturnTicket(returnTicket);
                routeDTO.setSumValue(departTicket.getValue() + returnTicket.getValue());
                routeDTO.setTripDurationInDays(departDate.until(returnDate).getDays());
                availableRoutes.add(routeDTO);
            }
        }
        availableRoutes.sort(Comparator.comparingInt(RouteDTO::getSumValue));
        return availableRoutes.subList(0, 100);
    }
    private List<RouteDTO> planOneWayTrip(String origin, String destination) {
        List<CheapTicket> departTickets = cheapTicketService.findByOriginAndDestination(origin, destination);
        List<RouteDTO> availableRoutes = new ArrayList<>();

        for (CheapTicket ticket : departTickets) {
            RouteDTO routeDTO = new RouteDTO();
            routeDTO.setOrigin(origin);
            routeDTO.setDestination(destination);
            routeDTO.setDepartTicket(ticket);
            routeDTO.setSumValue(ticket.getValue());
            availableRoutes.add(routeDTO);
        }
        availableRoutes.sort(Comparator.comparingInt(RouteDTO::getSumValue));
        return availableRoutes.subList(0, 100);
    }

    private List<RouteDTO> planReturnTripWithTripDurationFromTo(String origin, String destination, int from, int to) {
        List<RouteDTO> localRoutes = new ArrayList<>();
        for (int i = from; i <= to; i++) {
            List<RouteDTO> routeDTOS = planReturnTripForSpecificNumberOfDays(origin, destination, i);
            localRoutes.addAll(routeDTOS);
        }
        localRoutes.sort(Comparator.comparingInt(RouteDTO::getSumValue));
        return localRoutes.subList(0, 100);

    }
    private List<RouteDTO> planReturnTripForSpecificNumberOfDays(String origin, String destination, Integer tripDurationInDays) {
        List<CheapTicket> departTickets = cheapTicketService.findByOriginAndDestination(origin, destination);

        List<RouteDTO> availableRoutes = new ArrayList<>();
        for (CheapTicket departTicket : departTickets) {
            LocalDate departDate = departTicket.getDepartDate();
            LocalDate returnDate = departDate.plusDays(tripDurationInDays);
            List<CheapTicket> returnTickets = cheapTicketService.findByOriginAndDestinationAndDepartDate(origin, destination, returnDate);
            if (!returnTickets.isEmpty()) {
                Optional<CheapTicket> cheapestReturnTicket = returnTickets
                        .stream()
                        .filter(Objects::nonNull)
                        .min(Comparator.comparingInt(Ticket::getValue));

                if (cheapestReturnTicket.isPresent()) {
                    CheapTicket returnTicket = cheapestReturnTicket.get();
                    RouteDTO routeDTO = new RouteDTO();
                    routeDTO.setOrigin(origin);
                    routeDTO.setDestination(destination);
                    routeDTO.setDepartTicket(departTicket);
                    routeDTO.setReturnTicket(returnTicket);
                    routeDTO.setTripDurationInDays(tripDurationInDays);
                    routeDTO.setSumValue(departTicket.getValue() + returnTicket.getValue());
                    availableRoutes.add(routeDTO);
                }
            }
        }
        availableRoutes.sort(Comparator.comparingInt(RouteDTO::getSumValue));
        return availableRoutes.subList(0, 100);
    }

    private List<RouteDTO> planReturnTripWithDepartAndReturnDates(LocalDate departDate, LocalDate returnDate) {
        //TODO
        return Collections.emptyList();

    }

    private List<RouteDTO> planOneWayTripWithDepartDate(String origin, String destination, LocalDate departDate) {
        //TODO
        return Collections.emptyList();
    }

    private List<RouteDTO> planReturnTripAnywhereWithDepartReturnDate(String origin, LocalDate departDate, LocalDate returnDate) {
        //TODO
        return Collections.emptyList();
    }

    private List<RouteDTO> planReturnTripAnywhereWithDepartDateForSpecificNumberOfDays(String origin, String destionation, LocalDate departDate, int from) {
        //TODO
        return Collections.emptyList();
    }
}
