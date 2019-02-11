package com.example.tickets.route.filteringcriteria;

import com.example.tickets.route.Route;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Entity;
import javax.persistence.Transient;
import java.util.function.Predicate;

import static java.util.Objects.requireNonNull;

@Data
@AllArgsConstructor
@Entity
@EqualsAndHashCode(callSuper = true)
public class TripDurationFromCriteria extends RouteFilteringCriteria {
    @Transient
    private final Logger log = LoggerFactory.getLogger(TripDurationFromCriteria.class);
    private final Long tripDurationFrom;


    @Override
    public Predicate<? extends Route> filter(Route route) {
        log.debug("TripDurationFrom is {}", tripDurationFrom);
        log.debug("Route is {}", route);

        return (Predicate<Route>) route1 -> {
            var departTicket = route1.getDepartTicket();
            log.debug("Depart ticket: {}", departTicket);
            requireNonNull(departTicket, "Depart ticket shouldn't be null");

            var returnTicket = route1.getReturnTicket();
            log.debug("Return ticket: {}", returnTicket);
            requireNonNull(returnTicket, "Return ticket shouldn't be null");

            var departDate = departTicket.getDepartDate();
            log.debug("Depart date: {}", departDate);
            requireNonNull(departDate, "Depart date shouldn't be null");

            var returnDate = returnTicket.getDepartDate();
            log.debug("Return date: {}", returnDate);
            requireNonNull(returnDate, "Return date shouldn't be null");

            return departDate.until(returnDate).getDays() >= tripDurationFrom;
        };
    }
}
