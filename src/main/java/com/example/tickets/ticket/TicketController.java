package com.example.tickets.ticket;

import org.springframework.web.bind.annotation.RestController;

@RestController
public class TicketController {
//    private final Logger log = LoggerFactory.getLogger(TicketController.class);
//    private final TicketService service;
//
//    public TicketController(TicketService service) {
//        this.service = service;
//    }
//
//
//    @RequestMapping(value = "/ticket/cheapest", params = {"origin", "destination", "departureDate"})
//    public Optional<Ticket> cheapest(@RequestParam(value = "origin") String origin,
//                                     @RequestParam(value = "destination") String destination,
//                                     @RequestParam(value = "departureDate") String departureDate) {
//
//        return service.cheapest(origin, destination, LocalDate.parse(departureDate));
//    }
//
//    @RequestMapping(value = "/ticket/cheapest", params = {"origin", "destination"})
//    public Optional<Ticket> cheapest(@RequestParam(value = "origin") String origin,
//                                     @RequestParam(value = "destination") String destination) {
//
//        return service.cheapest(origin, destination);
//    }
//
//    @RequestMapping(sessionId = "/ticket/prices", params = {"origin", "destination", "departureDate"})
//    public Optional<ObjectNode> prices(@RequestParam(value = "origin") String origin,
//                                       @RequestParam(value = "destination") String destination,
//                                       @RequestParam(value = "departureDate") String departureDate) {
//
//
//        return service.prices(origin, destination, LocalDate.parse(departureDate));
//    }
//
//    @RequestMapping(sessionId = "/ticket/prices", params = {"origin", "destination"})
//    public Optional<ObjectNode> prices(@RequestParam(value = "origin") String origin,
//                                       @RequestParam(value = "destination") String destination) {
//
//        return service.prices(origin, destination);
//    }
}
