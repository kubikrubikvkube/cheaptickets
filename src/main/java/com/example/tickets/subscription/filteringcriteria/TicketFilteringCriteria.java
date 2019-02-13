package com.example.tickets.subscription.filteringcriteria;

import com.example.tickets.ticket.Ticket;
import lombok.Data;

import javax.persistence.*;
import java.util.function.Predicate;

@Entity
@Inheritance(
        strategy = InheritanceType.TABLE_PER_CLASS
)
@Data
public abstract class TicketFilteringCriteria {
    @Id
    @GeneratedValue
    private Long id;

    public abstract Predicate<Ticket> getPredicate();
}
