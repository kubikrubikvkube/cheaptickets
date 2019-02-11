package com.example.tickets.route.filteringcriteria;

import com.example.tickets.route.Route;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
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

    @CreationTimestamp
    private LocalDateTime creationTimestamp;

    abstract Predicate<Route> getPredicate();

    void ensureTicketFieldsAreNonNull(Route route) {
        var departTicket = route.getDepartTicket();
        requireNonNull(departTicket, "Depart ticket shouldn't be null");

        var returnTicket = route.getReturnTicket();
        requireNonNull(returnTicket, "Return ticket shouldn't be null");

        var departDate = departTicket.getDepartDate();
        requireNonNull(departDate, "Depart date shouldn't be null");

        var returnDate = returnTicket.getDepartDate();
        requireNonNull(returnDate, "Return date shouldn't be null");
    }
}
