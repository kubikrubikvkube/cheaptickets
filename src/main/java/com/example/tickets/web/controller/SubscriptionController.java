package com.example.tickets.web.controller;

import com.example.tickets.iata.IATA;
import com.example.tickets.iata.IATAService;
import com.example.tickets.subscription.Subscription;
import com.example.tickets.subscription.SubscriptionService;
import com.example.tickets.web.commandobjects.SubscriptionDTOCO;
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
    private final IATAService iataService;

    public SubscriptionController(SubscriptionService service, IATAService iataService) {
        this.subscriptionService = service;
        this.iataService = iataService;
    }

    @GetMapping("/admin/subscription")
    public String subscriptionPage(Model model) {
        model.addAttribute(new SubscriptionDTOCO());
        return SUBSCRIPTION_PAGE;
    }

    @PostMapping("/admin/subscription/add")
    public String subscriptionAdd(@ModelAttribute SubscriptionDTOCO subscriptionDTOCO) {
        IATA originIATA = iataService.fromPlaceName(subscriptionDTOCO.getOriginName());
        IATA destinationIATA = iataService.fromPlaceName(subscriptionDTOCO.getDestinationName());

        subscriptionDTOCO.setOrigin(originIATA.getCode());
        subscriptionDTOCO.setDestination(destinationIATA.getCode());
        subscriptionService.save(subscriptionDTOCO);
        return SUBSCRIPTION_PAGE;
    }

    @PostMapping("/admin/subscription/delete")
    public String subscriptionDelete(@ModelAttribute SubscriptionDTOCO subscriptionDTOCO) {
        Long id = subscriptionDTOCO.getId();
        Optional<Subscription> subscriptionOptional = subscriptionService.find(id);
        if (subscriptionOptional.isPresent()) {
            subscriptionService.delete(id);
        }
        return SUBSCRIPTION_PAGE;
    }

}
