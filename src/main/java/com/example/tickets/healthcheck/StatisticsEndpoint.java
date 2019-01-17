package com.example.tickets.healthcheck;

import com.example.tickets.owner.OwnerRepository;
import com.example.tickets.subscription.SubscriptionRepository;
import com.example.tickets.ticket.CheapTicketService;
import com.example.tickets.ticket.TicketRepository;
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
    private final TicketRepository ticketRep;
    private final SubscriptionRepository subscriptionRep;
    private final OwnerRepository ownerRep;
    private final CheapTicketService cheapTicketService;

    public StatisticsEndpoint(ObjectMapper mapper, TicketRepository ticketRep, SubscriptionRepository subscriptionRep, OwnerRepository ownerRep, CheapTicketService cheapTicketService) {
        this.mapper = mapper;
        this.ticketRep = ticketRep;
        this.subscriptionRep = subscriptionRep;
        this.ownerRep = ownerRep;
        this.cheapTicketService = cheapTicketService;
    }

    @ReadOperation
    public String getStatistics() throws JsonProcessingException {
        ObjectNode root = mapper.createObjectNode();
        root.set("tickets", prepareTicketsMetric());
        root.set("subscriptions", prepareSubscriptionsMetric());
        root.set("owners", prepareOwnerMetric());
        root.set("cheap tickets", prepareCheapTicketMetric());
        root.put("timestamp", LocalDateTime.now().toString());
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(root);
    }

    private ObjectNode prepareTicketsMetric() {
        ObjectNode tickets = mapper.createObjectNode();
        tickets.put("total tickets", ticketRep.count());
        int ticketsWithUnknownExpirationStatusSize = ticketRep.findTicketsWithUnknownExpirationStatus().size();
        tickets.put("unknown expiration status tickets", ticketsWithUnknownExpirationStatusSize);
        int unaccountedExpiredTicketsSize = ticketRep.findExpiredTickets(LocalDate.now(), false).size();
        tickets.put("unaccounted expired tickets", unaccountedExpiredTicketsSize);
        int accountedExpiredTicketsSize = ticketRep.findExpiredTickets(LocalDate.now(), true).size();
        tickets.put("accounted expired tickets", accountedExpiredTicketsSize);
        return tickets;
    }

    private ObjectNode prepareSubscriptionsMetric() {
        ObjectNode subscriptions = mapper.createObjectNode();
        subscriptions.put("total", subscriptionRep.count());
        return subscriptions;
    }

    private ObjectNode prepareOwnerMetric() {
        ObjectNode owners = mapper.createObjectNode();
        owners.put("total", ownerRep.countDistinct());
        return owners;
    }

    private ObjectNode prepareCheapTicketMetric() {
        ObjectNode cheapTickets = mapper.createObjectNode();
        cheapTickets.put("total", cheapTicketService.count());
        return cheapTickets;
    }
}
