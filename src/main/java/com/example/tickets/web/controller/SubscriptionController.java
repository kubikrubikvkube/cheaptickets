package com.example.tickets.web.controller;

import com.example.tickets.iata.Iata;
import com.example.tickets.iata.IataService;
import com.example.tickets.subscription.Subscription;
import com.example.tickets.subscription.SubscriptionService;
import com.example.tickets.web.commandobjects.SubscriptionDtoCommandObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;


@Controller
public class SubscriptionController {
    private static final String SUBSCRIPTION_PAGE = "subscription";
    private final SubscriptionService subscriptionService;
    private final IataService iataService;

    public SubscriptionController(SubscriptionService service, IataService iataService) {
        this.subscriptionService = service;
        this.iataService = iataService;
    }

    @GetMapping("/admin/subscription")
    public String subscriptionPage(Model model) {
        model.addAttribute(new SubscriptionDtoCommandObject());
        return SUBSCRIPTION_PAGE;
    }

    @PostMapping("/admin/subscription/add")
    public String subscriptionAdd(@ModelAttribute SubscriptionDtoCommandObject subscriptionDtoCommandObject) {
        Iata originIata = iataService.fromPlaceName(subscriptionDtoCommandObject.getOriginName());
        Iata destinationIata = iataService.fromPlaceName(subscriptionDtoCommandObject.getDestinationName());

        subscriptionDtoCommandObject.setOrigin(originIata.getCode());
        subscriptionDtoCommandObject.setDestination(destinationIata.getCode());
        subscriptionService.save(subscriptionDtoCommandObject);
        return SUBSCRIPTION_PAGE;
    }

    @PostMapping("/admin/subscription/delete")
    public String subscriptionDelete(@ModelAttribute SubscriptionDtoCommandObject subscriptionDtoCommandObject) {
        Long id = subscriptionDtoCommandObject.getId();
        Optional<Subscription> subscriptionOptional = subscriptionService.find(id);
        if (subscriptionOptional.isPresent()) {
            subscriptionService.delete(id);
        }
        return SUBSCRIPTION_PAGE;
    }

}
