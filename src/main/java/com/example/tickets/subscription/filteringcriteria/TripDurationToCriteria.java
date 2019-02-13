package com.example.tickets.subscription.filteringcriteria;

import com.example.tickets.route.RouteDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Entity;
import javax.persistence.Transient;
import java.util.Objects;
import java.util.function.Predicate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EqualsAndHashCode(callSuper = true)
public class TripDurationToCriteria extends RouteFilteringCriteria {
    @Transient
    private static final Logger log = LoggerFactory.getLogger(TripDurationToCriteria.class);
    private Integer tripDurationTo;

    @Override
    public Predicate<RouteDto> getPredicate() {
        Objects.requireNonNull(tripDurationTo);
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
