package com.example.tickets.actuator.statistics;

public class StatisticsEndpoint {
//    private final ObjectMapper mapper;
//    private final TicketService ticketService;
//    private final SubscriptionService subscriptionService;
//    private final OwnerService ownerService;
//    private final CheapTicketService cheapTicketService;
//    private final TicketStatisticsService ticketStatisticsService;
//    private final RoutesService routesService;
//
//    public StatisticsEndpoint(ObjectMapper mapper, TicketService ticketService, SubscriptionService subscriptionService, OwnerService ownerService, CheapTicketService cheapTicketService, TicketStatisticsService ticketStatisticsService, RoutesService routesService) {
//        this.mapper = mapper;
//        this.ticketService = ticketService;
//        this.subscriptionService = subscriptionService;
//        this.ownerService = ownerService;
//        this.cheapTicketService = cheapTicketService;
//        this.ticketStatisticsService = ticketStatisticsService;
//        this.routesService = routesService;
//    }
//
//    @ReadOperation
//    public String getStatistics() throws JsonProcessingException {
//        ObjectNode root = mapper.createObjectNode();
//
//        root.set("subscriptions", prepareSubscriptionsMetric());
//        root.set("owners", prepareOwnerMetric());
//        root.set("cheap tickets", prepareCheapTicketMetric());
//        root.set("ticket statistics", prepareTicketStatisticsMetric());
//        root.set("routes", prepareRoutesMetric());
//        root.put("timestamp", LocalDateTime.now().toString());
//        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(root);
//    }
//
//
//    private ObjectNode prepareSubscriptionsMetric() {
//        ObjectNode subscriptions = mapper.createObjectNode();
//        subscriptions.put("total", subscriptionService.count());
//        return subscriptions;
//    }
//
//    private ObjectNode prepareOwnerMetric() {
//        ObjectNode owners = mapper.createObjectNode();
//        owners.put("total", ownerService.countDistinct());
//        return owners;
//    }
//
//    private ObjectNode prepareCheapTicketMetric() {
//        ObjectNode cheapTickets = mapper.createObjectNode();
//        cheapTickets.put("total", cheapTicketService.count());
//        return cheapTickets;
//    }
//
//    private ObjectNode prepareTicketStatisticsMetric() {
//        ObjectNode ticketStatistics = mapper.createObjectNode();
//        ticketStatistics.put("total", ticketStatisticsService.count());
//        return ticketStatistics;
//    }
//
//    private ObjectNode prepareRoutesMetric() {
//        ObjectNode routes = mapper.createObjectNode();
//        routes.put("total", routesService.count());
//        return routes;
//    }
}
