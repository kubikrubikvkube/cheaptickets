package com.example.tickets.web.controller;

import com.example.tickets.iata.Iata;
import com.example.tickets.iata.IataService;
import com.example.tickets.owner.Owner;
import com.example.tickets.owner.OwnerService;
import com.example.tickets.subscription.*;
import com.example.tickets.subscription.filteringcriteria.*;
import com.example.tickets.util.ServiceException;
import com.example.tickets.web.commandobjects.SubscriptionCommandObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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
        model.addAttribute("subscriptionCommandObject", new SubscriptionCommandObject());
        return MAIN_PAGE;
    }

    @PostMapping("/main/saveSubscription")
    public RedirectView saveSubscription(HttpSession session, @ModelAttribute SubscriptionCommandObject commandObject, Model model) {
        Optional<Owner> ownerOptional = (Optional<Owner>) session.getAttribute("ownerDto");
        if (ownerOptional.isEmpty()) throw new ServiceException("Owner is not found in model");
        Owner owner = ownerOptional.get();
        SubscriptionDto dto = new SubscriptionDto();
        dto.setOwner(owner);

        Iata originIata = iataService.fromPlaceName(commandObject.getOriginName());
        dto.setOrigin(originIata.getCode());
        dto.setOriginName(originIata.getPlace());

        Iata destinationIata = iataService.fromPlaceName(commandObject.getDestinationName());
        dto.setDestination(destinationIata.getCode());
        dto.setDestinationName(destinationIata.getPlace());

        TicketFilteringCriteria destinationCriteria = new TicketDestinationCriteria(destinationIata.getCode());
        RouteFilteringCriteria tripDurationFromCriteria = new TripDurationFromCriteria(commandObject.getTripDurationInDaysFrom());
        RouteFilteringCriteria tripDurationToCriteria = new TripDurationFromCriteria(commandObject.getTripDurationInDaysTo());
        RouteFilteringCriteria maxPriceFilteringCriteria = new MaxPriceFilteringCriteria(commandObject.getMaxPrice());

        dto.setTicketFilteringCriteriaSet(Set.of(destinationCriteria));
        dto.setRouteFilteringCriteriaSet(Set.of(tripDurationFromCriteria, tripDurationToCriteria, maxPriceFilteringCriteria));

        Optional<Subscription> foundSubscription = subscriptionService.find(dto);
        if (foundSubscription.isEmpty()) {
            Subscription savedSubscription = subscriptionService.save(dto);
            model.addAttribute("lastSavedSubscription", savedSubscription);
        }
        return new RedirectView(MAIN_PAGE);
    }

    @PostMapping("/main/deleteSubscription")
    public RedirectView deleteSubscription(HttpSession session, @ModelAttribute SubscriptionCommandObject subscriptionDto, Model model) {
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
        Object ownerAttribute = httpSession.getAttribute("ownerDto");
        if (ownerAttribute == null) throw new ServiceException("Owner is not found in model");
        Optional<Owner> ownerOptional = (Optional<Owner>) ownerAttribute;
        if (ownerOptional.isEmpty()) throw new ServiceException("Owner is found but optional is empty");
        Owner owner = ownerOptional.get();
        return subscriptionService.get(owner.getEmail());
    }

}
