package com.example.tickets.web.controller;

import com.example.tickets.iata.IATAResolver;
import com.example.tickets.owner.Owner;
import com.example.tickets.owner.OwnerService;
import com.example.tickets.subscription.*;
import com.example.tickets.util.ServiceException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

@Controller
public class MainController {
    private final String MAIN_PAGE = "main";
    private final String LOGIN_PAGE = "login";

    private final SubscriptionService subscriptionService;
    private final SubscriptionDTOMapper mapper;
    private final IATAResolver iataResolver;
    private final SubscriptionTypeResolver typeResolver;
    private final OwnerService ownerService;

    public MainController(SubscriptionService subscriptionService, SubscriptionDTOMapper mapper, IATAResolver iataResolver, SubscriptionTypeResolver typeResolver, OwnerService ownerService) {
        this.subscriptionService = subscriptionService;
        this.mapper = mapper;
        this.iataResolver = iataResolver;
        this.typeResolver = typeResolver;
        this.ownerService = ownerService;
    }


    @GetMapping("/main")
    public String main(HttpSession httpSession, Model model) {
        model.addAttribute("lastSavedSubscription", null);
        model.addAttribute("subscriptionDTO", new SubscriptionDTO());
        model.addAttribute("ownerSubscriptions", ownerSubscriptions(httpSession));
        return MAIN_PAGE;
    }

    @PostMapping("/main/saveSubscription")
    public String saveSubscription(HttpSession session, @ModelAttribute SubscriptionDTO subscriptionDTO, Model model) {
        Optional<Owner> ownerOptional = (Optional<Owner>) session.getAttribute("ownerDTO");
        if (ownerOptional.isEmpty()) throw new ServiceException("Owner is not found in model");
        Owner owner = ownerOptional.get();
        subscriptionDTO.setOwner(owner);
        subscriptionDTO = iataResolver.resolve(subscriptionDTO);
        SubscriptionType subscriptionType = typeResolver.resolve(subscriptionDTO);
        subscriptionDTO.setSubscriptionType(subscriptionType);
        Optional<Subscription> foundSubscription = subscriptionService.find(subscriptionDTO);
        if (foundSubscription.isEmpty()) {
            Subscription savedSubscription = subscriptionService.save(subscriptionDTO);
            model.addAttribute("lastSavedSubscription", savedSubscription);
        }
        return MAIN_PAGE;
    }

    @ModelAttribute("ownerSubscriptions")
    public List<Subscription> ownerSubscriptions(HttpSession httpSession) {
        Optional<Owner> ownerOptional = (Optional<Owner>) httpSession.getAttribute("ownerDTO");
        if (ownerOptional.isEmpty()) throw new ServiceException("Owner is not found in model");
        Owner owner = ownerOptional.get();
        return subscriptionService.get(owner.getEmail());
    }

}
