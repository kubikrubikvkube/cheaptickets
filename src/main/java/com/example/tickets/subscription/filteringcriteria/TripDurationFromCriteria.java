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
@Entity
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TripDurationFromCriteria extends RouteFilteringCriteria {
    @Transient
    private static final Logger log = LoggerFactory.getLogger(TripDurationFromCriteria.class);
    private Integer tripDurationFrom;


    @Override
    public Predicate<RouteDto> getPredicate() {
        Objects.requireNonNull(tripDurationFrom);
        return route -> {
            log.debug("TripDurationFrom is {}", tripDurationFrom);
            log.debug("Route is {}", route);
            ensureTicketFieldsAreNonNull(route);

            var departDate = route.getDepartTicket().getDepartDate();
            var returnDate = route.getReturnTicket().getDepartDate();

            return departDate.until(returnDate).getDays() >= tripDurationFrom;
        };
    }
}
