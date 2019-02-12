package com.example.tickets.subscription.filteringcriteria;

import com.example.tickets.ticket.Ticket;
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
public class TicketDestinationCriteria extends TicketFilteringCriteria {
    @Transient
    private static final Logger log = LoggerFactory.getLogger(TripDurationFromCriteria.class);

    private String destination;


    @Override
    public Predicate<Ticket> getPredicate() {
        Objects.requireNonNull(destination);
        return ticket -> ticket.getDestination().equalsIgnoreCase(destination);
    }
}
