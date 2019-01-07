package com.example.tickets.healthcheck;

import com.example.tickets.subscription.SubscriptionRepository;
import com.example.tickets.ticket.TicketRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Component
@Endpoint(id = "statistics")
public class StatisticsEndpoint {
    private final ObjectMapper mapper;
    private final TicketRepository ticketRep;
    private final SubscriptionRepository subscriptionRep;

    public StatisticsEndpoint(ObjectMapper mapper, TicketRepository ticketRep, SubscriptionRepository subscriptionRep) {
        this.mapper = mapper;
        this.ticketRep = ticketRep;
        this.subscriptionRep = subscriptionRep;
    }

    @ReadOperation
    public String getStatistics() throws JsonProcessingException {
        ObjectNode root = mapper.createObjectNode();
        root.set("tickets", prepareTicketsMetric());
        root.set("subscriptions", prepareSubscriptionsMetric());
        root.put("timestamp", LocalDateTime.now().toString());
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(root);
    }

    private ObjectNode prepareTicketsMetric() {
        ObjectNode tickets = mapper.createObjectNode();
        tickets.put("total tickets", ticketRep.count());
        int unaccountedExpiredTicketsSize = ticketRep.findExpiredTickets(LocalDate.now(), LocalTime.now(), false).size();
        tickets.put("unaccounted expired tickets", unaccountedExpiredTicketsSize);
        int accountedExpiredTicketsSize = ticketRep.findExpiredTickets(LocalDate.now(), LocalTime.now(), true).size();
        tickets.put("accounted expired tickets", accountedExpiredTicketsSize);
        return tickets;
    }

    private ObjectNode prepareSubscriptionsMetric() {
        ObjectNode subscriptions = mapper.createObjectNode();
        subscriptions.put("total subscriptions", subscriptionRep.count());
        subscriptions.put("unexpired subscriptions", subscriptionRep.countNonExpired());
        return subscriptions;
    }
}
