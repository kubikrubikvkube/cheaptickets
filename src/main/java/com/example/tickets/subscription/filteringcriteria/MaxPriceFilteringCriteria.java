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
import java.util.function.Predicate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EqualsAndHashCode(callSuper = true)
public class MaxPriceFilteringCriteria extends RouteFilteringCriteria {
    @Transient
    private static final Logger log = LoggerFactory.getLogger(MaxPriceFilteringCriteria.class);
    private Long maxPrice;


    @Override
    public Predicate<RouteDto> getPredicate() {
        return route -> route.getSumValue() <= maxPrice;
    }
}
