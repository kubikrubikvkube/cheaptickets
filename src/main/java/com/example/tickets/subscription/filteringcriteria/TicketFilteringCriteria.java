package com.example.tickets.subscription.filteringcriteria;

import com.example.tickets.ticket.Ticket;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
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

    @CreationTimestamp
    private LocalDateTime creationTimestamp;

    public abstract Predicate<Ticket> getPredicate();
}
