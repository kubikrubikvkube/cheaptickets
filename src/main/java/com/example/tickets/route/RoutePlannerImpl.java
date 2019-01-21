package com.example.tickets.route;

import com.example.tickets.subscription.Subscription;
import com.example.tickets.ticket.CheapTicket;
import com.example.tickets.ticket.CheapTicketService;
import com.example.tickets.ticket.Ticket;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class RoutePlannerImpl implements RoutePlanner {
    private final CheapTicketService cheapTicketService;

    public RoutePlannerImpl(CheapTicketService cheapTicketService) {
        this.cheapTicketService = cheapTicketService;
    }

    @Override
    public List<RouteDTO> plan(Subscription subscription) {
        String origin = subscription.getOrigin();
        String destination = subscription.getDestination();
        LocalDate departDate = subscription.getDepartDate();
        Integer tripDuration = subscription.getTripDurationInDays();
        LocalDate returnDate = subscription.getReturnDate();
        if ((origin != null && destination != null && tripDuration != null) && (departDate == null && returnDate == null)) {
            return planReturnTripForSpecificNumberOfDays(origin, destination, tripDuration);
        } else if ((origin != null && destination != null) && (tripDuration == null && departDate == null && returnDate == null)) {
            return planOneWayTrip(origin, destination);
        } else if ((origin != null && destination != null && departDate != null) && (tripDuration == null && returnDate == null)) {
            return planOneWayTrip(origin, destination).stream().filter(routeDTO -> routeDTO.getDepartTicket().getDepartDate().equals(departDate)).collect(Collectors.toList());
        } else if ((origin != null && destination != null && departDate != null && returnDate != null)) {
            return planReturnTripWithDepartAndReturnDates(origin, destination, departDate, returnDate);
        }
        return Collections.emptyList();
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
        return availableRoutes;
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
        return availableRoutes;
    }

    private List<RouteDTO> planReturnTripForSpecificNumberOfDays(String origin, String destination, Integer tripDurationInDays) {
        List<CheapTicket> departTickets = cheapTicketService.findByOriginAndDestination(origin, destination);

        List<RouteDTO> availableRoutes = new ArrayList<>();
        for (CheapTicket departTicket : departTickets) {
            LocalDate departDate = departTicket.getDepartDate();
            LocalDate returnDate = departDate.plusDays(tripDurationInDays);
            List<CheapTicket> returnTickets = cheapTicketService.findByOriginAndDestinationAndDepartDate(origin, destination, returnDate);
            if (returnTickets.size() > 0) {
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
        return availableRoutes;
    }
}
