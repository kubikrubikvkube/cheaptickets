package com.example.tickets.subscription;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@RestController
public class SubscriptionController {
    private final Logger log = LoggerFactory.getLogger(SubscriptionController.class);
    private final AtomicBoolean isStarted = new AtomicBoolean(false);
    private final SubscriptionRepository repository;
    private final ModelMapper modelMapper;

    public SubscriptionController(SubscriptionRepository repository, ModelMapper modelMapper) {
        this.repository = repository;
        this.modelMapper = modelMapper;
    }

    @RequestMapping(value = "/subscription/add", params = {"owner", "origin", "destination"})
    public List<Subscription> add(@RequestParam String owner,
                                  @RequestParam String origin,
                                  @RequestParam String destination) {

        boolean exists = repository.exists(owner, origin, destination);

        if (!exists) {
            SubscriptionDTO dto = new SubscriptionDTO(owner, origin, destination);
            Subscription subscription = modelMapper.map(dto, Subscription.class);
            repository.save(subscription);
        }

        return repository.findBy(owner, origin, destination);
    }

    @RequestMapping(value = "/subscription/get", params = {"owner", "origin", "destination"})
    public List<Subscription> get(@RequestParam String owner,
                                  @RequestParam String origin,
                                  @RequestParam String destination) {
        return repository.findBy(owner, origin, destination);
    }

    @RequestMapping(value = "/subscription/get", params = "owner")
    public List<Subscription> get(@RequestParam String owner) {
        return repository.findByOwner(owner);
    }

    @RequestMapping(value = "/subscription/delete", params = {"owner", "origin", "destination"})
    public void delete(@RequestParam String owner,
                       @RequestParam String origin,
                       @RequestParam String destination) {

        List<Subscription> foundSubscriptions = repository.findBy(owner, origin, destination);
        repository.deleteAll(foundSubscriptions);
    }

    @RequestMapping(value = "/subscription/delete", params = {"owner"})
    public void delete(@RequestParam String owner) {
        List<Subscription> foundSubscriptions = repository.findByOwner(owner);
        repository.deleteAll(foundSubscriptions);
    }


}
