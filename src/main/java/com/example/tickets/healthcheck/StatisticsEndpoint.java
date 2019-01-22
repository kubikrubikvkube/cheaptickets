package com.example.tickets.healthcheck;

import com.example.tickets.owner.OwnerService;
import com.example.tickets.route.RoutesService;
import com.example.tickets.statistics.TicketStatisticsService;
import com.example.tickets.subscription.SubscriptionService;
import com.example.tickets.ticket.CheapTicketService;
import com.example.tickets.ticket.TicketService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
@Endpoint(id = "statistics")
public class StatisticsEndpoint {
    private final ObjectMapper mapper;
    private final TicketService ticketService;
    private final SubscriptionService subscriptionService;
    private final OwnerService ownerService;
    private final CheapTicketService cheapTicketService;
    private final TicketStatisticsService ticketStatisticsService;
    private final RoutesService routesService;

    public StatisticsEndpoint(ObjectMapper mapper, TicketService ticketService, SubscriptionService subscriptionService, OwnerService ownerService, CheapTicketService cheapTicketService, TicketStatisticsService ticketStatisticsService, RoutesService routesService) {
        this.mapper = mapper;
        this.ticketService = ticketService;
        this.subscriptionService = subscriptionService;
        this.ownerService = ownerService;
        this.cheapTicketService = cheapTicketService;
        this.ticketStatisticsService = ticketStatisticsService;
        this.routesService = routesService;
    }

    @ReadOperation
    public String getStatistics() throws JsonProcessingException {
        ObjectNode root = mapper.createObjectNode();
        root.set("tickets", prepareTicketsMetric());
        root.set("subscriptions", prepareSubscriptionsMetric());
        root.set("owners", prepareOwnerMetric());
        root.set("cheap tickets", prepareCheapTicketMetric());
        root.set("ticket statistics", prepareTicketStatisticsMetric());
        root.set("routes", prepareRoutesMetric());
        root.put("timestamp", LocalDateTime.now().toString());
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(root);
    }

    private ObjectNode prepareTicketsMetric() {
        ObjectNode tickets = mapper.createObjectNode();
        tickets.put("total tickets", ticketService.count());
        long nonExpiredTicketsSize = ticketService.findNonExpiredTicketsSize();
        tickets.put("non expired tickets", nonExpiredTicketsSize);
        int accountedExpiredTicketsSize = ticketService.findExpiredTickets(LocalDate.now(), true).size();
        tickets.put("accounted expired tickets", accountedExpiredTicketsSize);
        int ticketsWithUnknownExpirationStatusSize = ticketService.findTicketsWithUnknownExpirationStatus().size();
        tickets.put("unknown expiration status tickets", ticketsWithUnknownExpirationStatusSize);
        int unaccountedExpiredTicketsSize = ticketService.findExpiredTickets(LocalDate.now(), false).size();
        tickets.put("unaccounted expired tickets", unaccountedExpiredTicketsSize);
        return tickets;
    }

    private ObjectNode prepareSubscriptionsMetric() {
        ObjectNode subscriptions = mapper.createObjectNode();
        subscriptions.put("total", subscriptionService.count());
        return subscriptions;
    }

    private ObjectNode prepareOwnerMetric() {
        ObjectNode owners = mapper.createObjectNode();
        owners.put("total", ownerService.countDistinct());
        return owners;
    }

    private ObjectNode prepareCheapTicketMetric() {
        ObjectNode cheapTickets = mapper.createObjectNode();
        cheapTickets.put("total", cheapTicketService.count());
        return cheapTickets;
    }

    private ObjectNode prepareTicketStatisticsMetric() {
        ObjectNode ticketStatistics = mapper.createObjectNode();
        ticketStatistics.put("total", ticketStatisticsService.count());
        return ticketStatistics;
    }

    private ObjectNode prepareRoutesMetric() {
        ObjectNode routes = mapper.createObjectNode();
        routes.put("total", routesService.count());
        return routes;
    }
}
