package com.example.tickets.subscription;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SubscriptionRestController {
    private static final Logger log = LoggerFactory.getLogger(SubscriptionRestController.class);
    private final SubscriptionService service;

    public SubscriptionRestController(SubscriptionService service) {
        this.service = service;
    }

    @RequestMapping(value = "/subscription/add", params = {"owner", "origin", "destination"})
    public List<Subscription> add(@RequestParam String owner,
                                  @RequestParam String origin,
                                  @RequestParam String destination) {
        log.info("Subscription add request for '{} {} {}'", owner, origin, destination);
        return service.add(owner, origin, destination);
    }

    @RequestMapping(value = "/subscription/add", params = {"owner", "origin", "destination", "departDate", "returnDate"})
    public List<Subscription> add(@RequestParam String owner,
                                  @RequestParam String origin,
                                  @RequestParam String destination,
                                  @RequestParam String departDate,
                                  @RequestParam String returnDate) {
        log.info("Subscription add request for '{} {} {} {} {}'", owner, origin, destination, departDate, returnDate);
        return service.add(owner, origin, destination, departDate, returnDate);
    }

    @RequestMapping(value = "/subscription/get", params = {"owner", "origin", "destination"})
    public List<Subscription> get(@RequestParam String owner,
                                  @RequestParam String origin,
                                  @RequestParam String destination) {
        log.info("Subscription get request '{} {} {}'", owner, origin, destination);
        return service.get(owner, origin, destination);
    }

    @RequestMapping(value = "/subscription/get", params = "owner")
    public List<Subscription> get(@RequestParam String owner) {
        log.info("Subscription get request '{}'", owner);
        return service.get(owner);
    }

    @RequestMapping(value = "/subscription/delete", params = {"owner", "origin", "destination"})
    public void delete(@RequestParam String owner,
                       @RequestParam String origin,
                       @RequestParam String destination) {
        log.info("Subscription delete request '{} {} {}'", owner, origin, destination);
        service.delete(owner, origin, destination);
    }

    @RequestMapping(value = "/subscription/delete", params = {"owner"})
    public void delete(@RequestParam String owner) {
        log.info("Subscription delete request '{}'", owner);
        service.delete(owner);
    }


}
