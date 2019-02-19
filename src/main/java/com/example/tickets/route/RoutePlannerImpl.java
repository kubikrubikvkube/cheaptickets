package com.example.tickets.route;

import com.example.tickets.subscription.Subscription;
import com.example.tickets.subscription.filteringcriteria.RouteFilteringCriteria;
import com.example.tickets.ticket.CheapTicket;
import com.example.tickets.ticket.CheapTicketService;
import com.example.tickets.ticket.Ticket;
import com.example.tickets.travelpayouts.AffilateLinkGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

@Component
public class RoutePlannerImpl implements RoutePlanner {
    private static final Logger log = LoggerFactory.getLogger(RoutePlannerImpl.class);
    private final CheapTicketService cheapTicketService;
    private final Integer MAXIMUM_REASONABLE_TRIP_TIME = 30;
    private final int MAX_ROUTES_TO_RETURN = 500;
    private final AffilateLinkGenerator affilateLinkGenerator;

    public RoutePlannerImpl(CheapTicketService cheapTicketService, AffilateLinkGenerator affilateLinkGenerator) {
        this.cheapTicketService = cheapTicketService;
        this.affilateLinkGenerator = affilateLinkGenerator;
    }

    @Override
    public List<RouteDto> plan(Subscription subscription) {
        requireNonNull(subscription, "Subscription shouldn't be null");
        var origin = subscription.getOrigin();
        requireNonNull(origin, "Subscription origin shouldn't be null");
        var destination = subscription.getDestination();
        requireNonNull(destination, "Subscription destination shouldn't be null");
        var tripDurationInDaysFrom = subscription.getTripDurationInDaysFrom();
        requireNonNull(tripDurationInDaysFrom, "Subscription TripDurationInDaysFrom shouldn't be null");
        var tripDurationInDaysTo = subscription.getTripDurationInDaysTo();
        requireNonNull(tripDurationInDaysTo, "Subscription TripDurationInDaysTo shouldn't be null");

        List<RouteDto> routeDtos = planReturnTripWithTripDurationFromTo(origin, destination, tripDurationInDaysFrom, tripDurationInDaysTo);

        Predicate<RouteDto> aggregatedRoutePredicate = subscription
                .getRouteFilteringCriteriaSet()
                .stream()
                .map(RouteFilteringCriteria::getPredicate)
                .reduce(x -> true, Predicate::and);

        List<RouteDto> bestRoutes = routeDtos
                .stream()
                .filter(aggregatedRoutePredicate)
                .sorted(Comparator.comparingInt(RouteDto::getSumValue))
                .limit(MAX_ROUTES_TO_RETURN)
                .collect(Collectors.toList());

        return bestRoutes;
    }

    private List<RouteDto> planReturnTripWithTripDurationFromTo(String origin, String destination, int from, int to) {
        List<RouteDto> availableRoutes = new ArrayList<>();
        for (int i = from; i <= to; i++) {
            List<RouteDto> routeDtos = internal_planReturnTripForSpecificNumberOfDays(origin, destination, i);
            availableRoutes.addAll(routeDtos);
        }

        return availableRoutes
                .stream()
                .sorted(Comparator.comparingInt(RouteDto::getSumValue))
                .collect(Collectors.toList());
    }

    private List<RouteDto> internal_planReturnTripForSpecificNumberOfDays(String origin, String destination, Integer tripDurationInDays) {
        List<CheapTicket> departTickets = cheapTicketService.findByOriginAndDestination(origin, destination);

        List<RouteDto> availableRoutes = new ArrayList<>();
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
                    RouteDto routeDto = new RouteDto();
                    routeDto.setOrigin(origin);
                    routeDto.setDestination(destination);
                    routeDto.setDepartTicket(departTicket);
                    routeDto.setReturnTicket(returnTicket);
                    routeDto.setTripDurationInDays(tripDurationInDays);
                    routeDto.setSumValue(departTicket.getValue() + returnTicket.getValue());
                    var affilateLink = affilateLinkGenerator.generate(routeDto);
                    routeDto.setAffilateLink(affilateLink);
                    availableRoutes.add(routeDto);
                }
            }
        }

        return availableRoutes;
    }
}
