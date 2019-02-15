package com.example.tickets.web.controller;

import com.example.tickets.iata.Iata;
import com.example.tickets.iata.IataService;
import com.example.tickets.owner.Owner;
import com.example.tickets.owner.OwnerService;
import com.example.tickets.subscription.*;
import com.example.tickets.subscription.filteringcriteria.MaxPriceFilteringCriteria;
import com.example.tickets.subscription.filteringcriteria.RouteFilteringCriteria;
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

import static java.util.Objects.requireNonNull;

@Controller
public class MainController {
    private final String MAIN_PAGE = "/main";
    private final String LOGIN_PAGE = "login";

    private final SubscriptionService subscriptionService;
    private final SubscriptionDtoMapper mapper;
    private final SubscriptionTypeResolver typeResolver;
    private final OwnerService ownerService;
    private final IataService iataService;
    private final static int DEFAULT_TRIP_DURATION_FROM = 2;
    private final static int DEFAULT_TRIP_DURATION_TO = 15;

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

        var placeName = requireNonNull(commandObject.getOriginName(), "Origin placeName shouldn't be null");
        Iata originIata = iataService.fromPlaceName(placeName);
        dto.setOrigin(originIata.getCode());
        dto.setOriginName(originIata.getPlace());

        var destinationPlaceName = requireNonNull(commandObject.getDestinationName(), "Destination originName shouldn't be null");
        Iata destinationIata = iataService.fromPlaceName(destinationPlaceName);
        dto.setDestination(destinationIata.getCode());
        dto.setDestinationName(destinationIata.getPlace());

        var tripDurationFrom = commandObject.getTripDurationInDaysFrom();
        dto.setTripDurationInDaysFrom(tripDurationFrom == null ? DEFAULT_TRIP_DURATION_FROM : tripDurationFrom);

        var tripDurationTo = commandObject.getTripDurationInDaysTo();
        dto.setTripDurationInDaysTo(tripDurationTo == null ? DEFAULT_TRIP_DURATION_TO : tripDurationTo);
        var maxPrice = commandObject.getMaxPrice();
        if (maxPrice != null) {
            RouteFilteringCriteria maxPriceFilteringCriteria = new MaxPriceFilteringCriteria(maxPrice);
            dto.setRouteFilteringCriteriaSet(Set.of(maxPriceFilteringCriteria));
        }

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
