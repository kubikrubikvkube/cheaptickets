package com.example.tickets.subscription.filteringcriteria;

import com.example.tickets.route.RouteDto;
import lombok.Data;

import javax.persistence.*;
import java.util.Objects;
import java.util.function.Predicate;

import static java.util.Objects.requireNonNull;

@Entity
@Inheritance(
        strategy = InheritanceType.TABLE_PER_CLASS
)
@Data
public abstract class RouteFilteringCriteria {
    @Id
    @GeneratedValue
    private Long id;

    public abstract Predicate<RouteDto> getPredicate();

    void ensureTicketFieldsAreNonNull(RouteDto route) {
        var departTicket = route.getDepartTicket();
        requireNonNull(departTicket, "Depart ticket shouldn't be null");

        var returnTicket = route.getReturnTicket();
        requireNonNull(returnTicket, "Return ticket shouldn't be null");

        var departDate = departTicket.getDepartDate();
        requireNonNull(departDate, "Depart date shouldn't be null");

        var returnDate = returnTicket.getDepartDate();
        requireNonNull(returnDate, "Return date shouldn't be null");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RouteFilteringCriteria)) return false;
        RouteFilteringCriteria that = (RouteFilteringCriteria) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
