package com.example.tickets.subscription.filteringcriteria;

import com.example.tickets.ticket.Ticket;
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
public class TicketDestinationCriteria extends TicketFilteringCriteria {
    @Transient
    private final Logger log = LoggerFactory.getLogger(TripDurationFromCriteria.class);

    private final String destination;


    @Override
    public Predicate<Ticket> getPredicate() {
        return ticket -> ticket.getDestination().equalsIgnoreCase(destination);
    }
}
