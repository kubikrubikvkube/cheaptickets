package com.example.tickets.subscription.filteringcriteria;

import com.example.tickets.route.Route;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Entity;
import javax.persistence.Transient;
import java.util.function.Predicate;

@Data
@AllArgsConstructor
@Entity
@EqualsAndHashCode(callSuper = true)
public class TripDurationToCriteria extends RouteFilteringCriteria {
    @Transient
    private final Logger log = LoggerFactory.getLogger(TripDurationToCriteria.class);
    private final Integer tripDurationTo;

    @Override
    Predicate<Route> getPredicate() {
        return route -> {
            log.debug("TripDurationTo is {}", tripDurationTo);
            log.debug("Route is {}", route);
            ensureTicketFieldsAreNonNull(route);

            var departDate = route.getDepartTicket().getDepartDate();
            var returnDate = route.getReturnTicket().getDepartDate();

            return departDate.until(returnDate).getDays() <= tripDurationTo;
        };
    }
}
