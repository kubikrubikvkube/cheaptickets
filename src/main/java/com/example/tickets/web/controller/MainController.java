package com.example.tickets.web.controller;

import com.example.tickets.iata.IATAResolver;
import com.example.tickets.owner.Owner;
import com.example.tickets.subscription.*;
import com.example.tickets.util.ServiceException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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

    public MainController(SubscriptionService subscriptionService, SubscriptionDTOMapper mapper, IATAResolver iataResolver, SubscriptionTypeResolver typeResolver) {
        this.subscriptionService = subscriptionService;
        this.mapper = mapper;
        this.iataResolver = iataResolver;
        this.typeResolver = typeResolver;
    }


    @GetMapping("/main")
    public String main(Model model, BindingResult bindingResult) {

        return MAIN_PAGE;
    }

    @PostMapping("/main/saveSubscription")
    public String saveSubscription(HttpSession session, @ModelAttribute SubscriptionDTO subscriptionDTO, Model model, BindingResult bindingResult) {
        Optional<Owner> ownerOptional = (Optional<Owner>) session.getAttribute("ownerDTO");
        if (ownerOptional.isEmpty()) throw new ServiceException("Owner is not found in model");
        Owner owner = ownerOptional.get();
        subscriptionDTO.setOwner(owner);
        subscriptionDTO = iataResolver.resolve(subscriptionDTO);
        SubscriptionType subscriptionType = typeResolver.resolve(subscriptionDTO);
        subscriptionDTO.setSubscriptionType(subscriptionType);
        List<Subscription> ownerSubscriptions = subscriptionService.get(owner.getEmail());
        ы
        return MAIN_PAGE;
    }


}
