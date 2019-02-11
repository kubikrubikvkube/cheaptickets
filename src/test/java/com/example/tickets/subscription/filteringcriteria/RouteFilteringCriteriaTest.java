package com.example.tickets.subscription.filteringcriteria;

import com.example.tickets.subscription.Subscription;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
class RouteFilteringCriteriaTest {

    @Test
    @Disabled
    void routeFilteringCriteriaShouldBeSetToSubscription() {
        Subscription s = new Subscription();
        Set<RouteFilteringCriteria> routeFilteringCriteriaSet = new HashSet<>();
        RouteFilteringCriteria from = new TripDurationFromCriteria(5);
        RouteFilteringCriteria to = new TripDurationToCriteria(10);
        routeFilteringCriteriaSet.add(from);
        routeFilteringCriteriaSet.add(to);
        s.setRouteFilteringCriteriaSet(routeFilteringCriteriaSet);
        assertNotNull(s.getRouteFilteringCriteriaSet());
        assertThat(s.getRouteFilteringCriteriaSet(), hasSize(2));
    }
}