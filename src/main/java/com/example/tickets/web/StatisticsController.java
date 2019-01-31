package com.example.tickets.web;

import com.example.tickets.owner.OwnerService;
import com.example.tickets.route.RoutesService;
import com.example.tickets.statistics.TicketStatisticsService;
import com.example.tickets.subscription.SubscriptionService;
import com.example.tickets.ticket.CheapTicketService;
import com.example.tickets.ticket.TicketService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StatisticsController {
    private static final String STATISTICS_PAGE = "statistics";
    private final ObjectMapper mapper;
    private final TicketService ticketService;
    private final SubscriptionService subscriptionService;
    private final OwnerService ownerService;
    private final CheapTicketService cheapTicketService;
    private final TicketStatisticsService ticketStatisticsService;
    private final RoutesService routesService;


    public StatisticsController(ObjectMapper mapper, TicketService ticketService, SubscriptionService subscriptionService, OwnerService ownerService, CheapTicketService cheapTicketService, TicketStatisticsService ticketStatisticsService, RoutesService routesService) {
        this.ticketService = ticketService;
        this.subscriptionService = subscriptionService;
        this.ownerService = ownerService;
        this.cheapTicketService = cheapTicketService;
        this.ticketStatisticsService = ticketStatisticsService;
        this.routesService = routesService;
        this.mapper = new ObjectMapper();
        this.mapper.enable(SerializationFeature.INDENT_OUTPUT);
    }


    @GetMapping("/admin/statistics")
    public String adminPage(Model model) {
        return STATISTICS_PAGE;
    }
}
