package com.example.tickets.web.controller;

import com.example.tickets.iata.Iata;
import com.example.tickets.iata.IataService;
import com.example.tickets.owner.Owner;
import com.example.tickets.owner.OwnerService;
import com.example.tickets.subscription.*;
import com.example.tickets.util.ServiceException;
import com.example.tickets.web.commandobjects.SubscriptionDtoCommandObject;
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
    private final SubscriptionDtoMapper mapper;
    private final SubscriptionTypeResolver typeResolver;
    private final OwnerService ownerService;
    private final IataService iataService;

    public MainController(SubscriptionService subscriptionService, SubscriptionDtoMapper mapper, SubscriptionTypeResolver typeResolver, OwnerService ownerService, IataService iataService) {
        this.subscriptionService = subscriptionService;
        this.mapper = mapper;
        this.typeResolver = typeResolver;
        this.ownerService = ownerService;
        this.iataService = iataService;
    }


    @GetMapping("/main")
    public String main(HttpSession httpSession, Model model) {
        model.addAttribute("lastSavedSubscription", null);
        model.addAttribute("subscriptionDto", new SubscriptionDtoCommandObject());
        return MAIN_PAGE;
    }

    @PostMapping("/main/saveSubscription")
    public RedirectView saveSubscription(HttpSession session, @ModelAttribute SubscriptionDto subscriptionDto, Model model) {
        Optional<Owner> ownerOptional = (Optional<Owner>) session.getAttribute("ownerDto");
        if (ownerOptional.isEmpty()) throw new ServiceException("Owner is not found in model");
        Owner owner = ownerOptional.get();
        subscriptionDto.setOwner(owner);
        Iata originIata = iataService.fromPlaceName(subscriptionDto.getOriginName());
        Iata destinationIata = iataService.fromPlaceName(subscriptionDto.getDestinationName());

        subscriptionDto.setOrigin(originIata.getCode());
        subscriptionDto.setDestination(destinationIata.getCode());
        SubscriptionType subscriptionType = typeResolver.resolve(subscriptionDto);
        subscriptionDto.setSubscriptionType(subscriptionType);
        Optional<Subscription> foundSubscription = subscriptionService.find(subscriptionDto);
        if (foundSubscription.isEmpty()) {
            Subscription savedSubscription = subscriptionService.save(subscriptionDto);
            model.addAttribute("lastSavedSubscription", savedSubscription);
        }
        return new RedirectView(MAIN_PAGE);
    }

    @PostMapping("/main/deleteSubscription")
    public RedirectView deleteSubscription(HttpSession session, @ModelAttribute SubscriptionDtoCommandObject subscriptionDto, Model model) {
        Optional<Subscription> subscriptionOptional = subscriptionService.find(subscriptionDto.getId());
        if (subscriptionOptional.isPresent()) {
            Subscription subscription = subscriptionOptional.get();
            Long subscriptionId = subscription.getId();
            subscriptionService.delete(subscriptionId);
        }
        return new RedirectView(MAIN_PAGE);
    }

    @ModelAttribute("ownerSubscriptions")
    public List<Subscription> ownerSubscriptions(HttpSession httpSession) {
        Optional<Owner> ownerOptional = (Optional<Owner>) httpSession.getAttribute("ownerDto");
        if (ownerOptional.isEmpty()) throw new ServiceException("Owner is not found in model");
        Owner owner = ownerOptional.get();
        return subscriptionService.get(owner.getEmail());
    }

}
