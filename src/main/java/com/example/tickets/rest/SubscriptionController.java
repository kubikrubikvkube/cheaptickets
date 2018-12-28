package com.example.tickets.rest;

import com.example.tickets.repository.subscription.Subscription;
import com.example.tickets.repository.subscription.SubscriptionRepository;
import com.example.tickets.service.subscription.SubscriptionDTO;
import lombok.extern.java.Log;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@RestController
@Log
public class SubscriptionController {
    private final AtomicBoolean isStarted = new AtomicBoolean(false);
    @Autowired
    SubscriptionRepository repository;
    @Autowired
    ModelMapper modelMapper;

    @RequestMapping("/subscription/add")
    public List<Subscription> add(@RequestParam String owner,
                                  @RequestParam String origin,
                                  @RequestParam String destination,
                                  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                  @RequestParam(required = false) Date departDate,
                                  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                  @RequestParam(required = false) Date returnDate,
                                  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                  @RequestParam(required = false) Date expirationDate) {

        boolean exists = repository.exists(owner, origin, destination, departDate, returnDate, expirationDate);

        if (!exists) {
            SubscriptionDTO dto = new SubscriptionDTO(owner, origin, destination);
            if (departDate != null) dto.setDepartDate(departDate);
            if (returnDate != null) dto.setReturnDate(returnDate);
            if (expirationDate != null) dto.setExpirationDate(expirationDate);

            Subscription subscription = modelMapper.map(dto, Subscription.class);
            repository.save(subscription);
        }
        return repository.findByOwnerAndOriginAndDestinationAndDepartDateAndReturnDateAndExpirationDate(owner, origin, destination, departDate, returnDate, expirationDate);
    }

    @RequestMapping(value = "/subscription/get")
    public List<Subscription> get(@RequestParam String owner,
                                  @RequestParam String origin,
                                  @RequestParam String destination) {

        return repository.findByOwnerAndOriginAndDestination(owner, origin, destination);
    }

    @RequestMapping(value = "/subscription/start")
    public void start() {
        if (isStarted.get()) return;
        log.info("Started at " + LocalDateTime.now());
        isStarted.set(true);
    }


}
