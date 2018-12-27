package com.example.tickets.rest;

import com.example.tickets.repository.subscription.Subscription;
import com.example.tickets.repository.subscription.SubscriptionRepository;
import com.example.tickets.service.subscription.SubscriptionDTO;
import lombok.extern.java.Log;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Log
public class SubscriptionController {
    @Autowired
    SubscriptionRepository repository;
    @Autowired
    ModelMapper modelMapper;

    @RequestMapping("/subscription/add")
    public Subscription add(@RequestParam(value = "owner") String owner,
                            @RequestParam(value = "origin") String origin,
                            @RequestParam(value = "destination") String destination) {

        boolean exists = repository.existsByOwnerAndOriginAndDestination(owner, origin, destination);
        if (!exists) {
            SubscriptionDTO dto = new SubscriptionDTO(owner, origin, destination);
            Subscription subscription = modelMapper.map(dto, Subscription.class);
            repository.save(subscription);
        }
        return repository.findByOwnerAndOriginAndDestination(owner, origin, destination);
    }
}
