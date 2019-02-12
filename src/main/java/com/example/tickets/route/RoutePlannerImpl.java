package com.example.tickets.route;

import com.example.tickets.subscription.Subscription;
import com.example.tickets.subscription.SubscriptionType;
import com.example.tickets.subscription.filteringcriteria.TicketFilteringCriteria;
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

@Component
public class RoutePlannerImpl implements RoutePlanner {
    private static final Logger log = LoggerFactory.getLogger(RoutePlannerImpl.class);
    private final CheapTicketService cheapTicketService;
    private final Integer MAXIMUM_REASONABLE_TRIP_TIME = 30;
    private final AffilateLinkGenerator affilateLinkGenerator;

    public RoutePlannerImpl(CheapTicketService cheapTicketService, AffilateLinkGenerator affilateLinkGenerator) {
        this.cheapTicketService = cheapTicketService;
        this.affilateLinkGenerator = affilateLinkGenerator;
    }

    @Override
    public List<RouteDto> plan(Subscription subscription) {
        List<CheapTicket> all = cheapTicketService.findAll();

        Predicate<Ticket> aggregatedTicketPredicate = subscription
                .getTicketFilteringCriteriaSet()
                .stream()
                .map(TicketFilteringCriteria::getPredicate)
                .reduce(x -> true, Predicate::and);

        List<CheapTicket> filteredTickets = all
                .stream()
                .filter(aggregatedTicketPredicate)
                .collect(Collectors.toList());
        //
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

        List<RouteDto> routes = new ArrayList<>();
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

    private List<RouteDto> planReturnTripWithDepartAndReturnDates(String origin, String destination, LocalDate departDate, LocalDate returnDate) {
        List<CheapTicket> departTickets = cheapTicketService.findByOriginAndDestinationAndDepartDate(origin, destination, departDate);
        List<CheapTicket> returnTickets = cheapTicketService.findByOriginAndDestinationAndDepartDate(destination, origin, returnDate);
        List<CheapTicket> departTicketsMutable = new ArrayList<>(departTickets);
        List<CheapTicket> returnTicketsMutable = new ArrayList<>(returnTickets);
        departTicketsMutable.sort(Comparator.comparingInt(Ticket::getValue));
        returnTicketsMutable.sort(Comparator.comparingInt(Ticket::getValue));

        List<RouteDto> availableRoutes = new ArrayList<>();
        for (CheapTicket departTicket : departTicketsMutable) {
            for (CheapTicket returnTicket : returnTicketsMutable) {
                RouteDto routeDto = new RouteDto();
                routeDto.setOrigin(origin);
                routeDto.setDestination(destination);
                routeDto.setDepartTicket(departTicket);
                routeDto.setReturnTicket(returnTicket);
                routeDto.setSumValue(departTicket.getValue() + returnTicket.getValue());
                routeDto.setTripDurationInDays(departDate.until(returnDate).getDays());
                availableRoutes.add(routeDto);
            }
        }
        availableRoutes.sort(Comparator.comparingInt(RouteDto::getSumValue));
        return availableRoutes.subList(0, 100);
    }

    private List<RouteDto> planOneWayTrip(String origin, String destination) {
        List<CheapTicket> departTickets = cheapTicketService.findByOriginAndDestination(origin, destination);
        List<RouteDto> availableRoutes = new ArrayList<>();

        for (CheapTicket ticket : departTickets) {
            RouteDto routeDto = new RouteDto();
            routeDto.setOrigin(origin);
            routeDto.setDestination(destination);
            routeDto.setDepartTicket(ticket);
            routeDto.setSumValue(ticket.getValue());
            availableRoutes.add(routeDto);
        }

        return availableRoutes
                .stream()
                .sorted(Comparator.comparingInt(RouteDto::getSumValue))
                .collect(Collectors.toList());
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

    private List<RouteDto> planReturnTripWithDepartAndReturnDates(LocalDate departDate, LocalDate returnDate) {
        //TODO
        return Collections.emptyList();

    }

    private List<RouteDto> planOneWayTripWithDepartDate(String origin, String destination, LocalDate departDate) {
        //TODO
        return Collections.emptyList();
    }

    private List<RouteDto> planReturnTripAnywhereWithDepartReturnDate(String origin, LocalDate departDate, LocalDate returnDate) {
        //TODO
        return Collections.emptyList();
    }

    private List<RouteDto> planReturnTripAnywhereWithDepartDateForSpecificNumberOfDays(String origin, String destionation, LocalDate departDate, int from) {
        //TODO
        return Collections.emptyList();
    }
}
