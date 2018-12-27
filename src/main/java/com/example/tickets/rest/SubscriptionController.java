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

import java.util.Date;
import java.util.List;

import static com.example.tickets.util.DateConverter.toDate;

@RestController
@Log
public class SubscriptionController {
    @Autowired
    SubscriptionRepository repository;
    @Autowired
    ModelMapper modelMapper;

    @RequestMapping("/subscription/add")
    public List<Subscription> add(@RequestParam(value = "owner") String owner,
                                  @RequestParam(value = "origin") String origin,
                                  @RequestParam(value = "destination") String destination,
                                  @RequestParam(value = "departDate", required = false) String departDateString,
                                  @RequestParam(value = "returnDate", required = false) String returnDateString,
                                  @RequestParam(value = "expirationDate", required = false) String expirationDateString) {

        Date departDate = toDate(departDateString);
        Date returnDate = toDate(returnDateString);
        Date expirationDate = toDate(expirationDateString);

        boolean exists = repository.existsByOwnerAndOriginAndDestinationAndDepartDateAndReturnDateAndExpirationDate(owner, origin, destination, departDate, returnDate, expirationDate);

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
}
