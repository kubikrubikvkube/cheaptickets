package com.example.tickets.web.controller;

import com.example.tickets.iata.IATA;
import com.example.tickets.iata.IataService;
import com.example.tickets.owner.Owner;
import com.example.tickets.owner.OwnerService;
import com.example.tickets.subscription.*;
import com.example.tickets.util.ServiceException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

@Controller
public class MainController {
    private final String MAIN_PAGE = "/main";
    private final String LOGIN_PAGE = "login";

    private final SubscriptionService subscriptionService;
    private final SubscriptionDTOMapper mapper;
    private final SubscriptionTypeResolver typeResolver;
    private final OwnerService ownerService;
    private final IataService iataService;

    public MainController(SubscriptionService subscriptionService, SubscriptionDTOMapper mapper, SubscriptionTypeResolver typeResolver, OwnerService ownerService, IataService iataService) {
        this.subscriptionService = subscriptionService;
        this.mapper = mapper;
        this.typeResolver = typeResolver;
        this.ownerService = ownerService;
        this.iataService = iataService;
    }


    @GetMapping("/main")
    public String main(HttpSession httpSession, Model model) {
        model.addAttribute("lastSavedSubscription", null);
        model.addAttribute("subscriptionDTO", new SubscriptionDTO());
        return MAIN_PAGE;
    }

    @PostMapping("/main/saveSubscription")
    public RedirectView saveSubscription(HttpSession session, @ModelAttribute SubscriptionDTO subscriptionDTO, Model model) {
        Optional<Owner> ownerOptional = (Optional<Owner>) session.getAttribute("ownerDTO");
        if (ownerOptional.isEmpty()) throw new ServiceException("Owner is not found in model");
        Owner owner = ownerOptional.get();
        subscriptionDTO.setOwner(owner);
        IATA originIATA = iataService.fromPlaceName(subscriptionDTO.getOriginName());
        IATA destinationIATA = iataService.fromPlaceName(subscriptionDTO.getDestinationName());

        subscriptionDTO.setOrigin(originIATA.getCode());
        subscriptionDTO.setDestination(destinationIATA.getCode());
        SubscriptionType subscriptionType = typeResolver.resolve(subscriptionDTO);
        subscriptionDTO.setSubscriptionType(subscriptionType);
        Optional<Subscription> foundSubscription = subscriptionService.find(subscriptionDTO);
        if (foundSubscription.isEmpty()) {
            Subscription savedSubscription = subscriptionService.save(subscriptionDTO);
            model.addAttribute("lastSavedSubscription", savedSubscription);
        }
        return new RedirectView(MAIN_PAGE);
    }

    @PostMapping("/main/deleteSubscription")
    public RedirectView deleteSubscription(HttpSession session, @ModelAttribute SubscriptionDTO subscriptionDTO, Model model) {
        Optional<Subscription> subscriptionOptional = subscriptionService.find(subscriptionDTO);
        if (subscriptionOptional.isPresent()) {
            Subscription subscription = subscriptionOptional.get();
            Long subscriptionId = subscription.getId();
            subscriptionService.delete(subscriptionId);
        }
        return new RedirectView(MAIN_PAGE);
    }

    @ModelAttribute("ownerSubscriptions")
    public List<Subscription> ownerSubscriptions(HttpSession httpSession) {
        Optional<Owner> ownerOptional = (Optional<Owner>) httpSession.getAttribute("ownerDTO");
        if (ownerOptional.isEmpty()) throw new ServiceException("Owner is not found in model");
        Owner owner = ownerOptional.get();
        return subscriptionService.get(owner.getEmail());
    }

}
