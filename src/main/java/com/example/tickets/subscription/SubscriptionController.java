package com.example.tickets.subscription;

import com.example.tickets.route.RoutePlanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SubscriptionController {
    private final Logger log = LoggerFactory.getLogger(SubscriptionController.class);
    private final SubscriptionService service;
    private final RoutePlanner routePlanner;

    public SubscriptionController(SubscriptionService service, RoutePlanner routePlanner) {
        this.service = service;
        this.routePlanner = routePlanner;
    }

    @RequestMapping(value = "/subscription/add", params = {"owner", "origin", "destination"})
    public List<Subscription> add(@RequestParam String owner,
                                  @RequestParam String origin,
                                  @RequestParam String destination) {
        log.info("Subscription add request for '{} {} {}'", owner, origin, destination);
        return service.add(owner, origin, destination);
    }

//    @RequestMapping(value = "/subscription/add", params = {"owner", "origin", "destination", "tripDurationInDays"})
//    public List<Subscription> add(@RequestParam String owner,
//                                  @RequestParam String origin,
//                                  @RequestParam String destination,
//                                  @RequestParam String tripDurationInDays) {
//        log.info("Subscription add request for '{} {} {} {}'", owner, origin, destination, tripDurationInDays);
//        List<Subscription> add = service.add(owner, origin, destination, tripDurationInDays);
//        Subscription subscription = add.get(0);
//        Route plan = routePlanner.plan(subscription);
//        return null;
//    }

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
