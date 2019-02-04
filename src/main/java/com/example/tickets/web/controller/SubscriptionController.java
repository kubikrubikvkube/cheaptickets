package com.example.tickets.web.controller;

import com.example.tickets.iata.IATAService;
import com.example.tickets.subscription.SubscriptionDTO;
import com.example.tickets.subscription.SubscriptionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
public class SubscriptionController {
    private static final String SUBSCRIPTION_PAGE = "subscription";
    private final SubscriptionService service;
    private final IATAService iataService;

    public SubscriptionController(SubscriptionService service, IATAService iataService) {
        this.service = service;
        this.iataService = iataService;
    }

    @GetMapping("/admin/subscription")
    public String subscriptionPage(Model model) {
        model.addAttribute("subscriptionDTO", new SubscriptionDTO());
        return SUBSCRIPTION_PAGE;
    }

    @PostMapping("/admin/subscription/add")
    public String subscriptionAdd(@ModelAttribute SubscriptionDTO subscriptionDTO) {
        var originIATA = iataService.fromPlaceName(subscriptionDTO.getOriginName());
        var destinationIATA = iataService.fromPlaceName(subscriptionDTO.getDestinationName());
        subscriptionDTO.setOrigin(originIATA);
        subscriptionDTO.setDestination(destinationIATA);
        service.save(subscriptionDTO);
        return SUBSCRIPTION_PAGE;
    }

    @PostMapping("/admin/subscription/delete")
    public String subscriptionDelete(@ModelAttribute SubscriptionDTO subscriptionDTO) {
        Long id = subscriptionDTO.getId();
        service.delete(id);
        return SUBSCRIPTION_PAGE;
    }

}
