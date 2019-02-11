package com.example.tickets.route.filteringcriteria;

import com.example.tickets.route.Route;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.function.Predicate;

@Entity
@Inheritance(
        strategy = InheritanceType.TABLE_PER_CLASS
)
public abstract class RouteFilteringCriteria {
    @Id
    @GeneratedValue
    private Long id;

    @CreationTimestamp
    private LocalDateTime creationTimestamp;

    abstract Predicate<? extends Route> filter(Route route);
}
