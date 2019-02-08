package com.example.tickets.util;

import com.example.tickets.route.Route;

import java.util.Comparator;

public class RouteComparators {
    private final static int PRICE_WEIGHT = 80;
    private final static int NUMBER_OF_CHANGES_WEIGHT = 20;

    public final static Comparator<Route> WEIGHTED_SUM_MODEL = (r1, r2) -> {
        var r1Weight = priceWeight(r1) - numberOfChangesWeight(r1);
        var r2Weight = priceWeight(r2) - numberOfChangesWeight(r2);
        return Integer.compare(r1Weight, r2Weight);
    };

    private RouteComparators() {

    }

    private static int priceWeight(Route route) {
        var departTicket = route.getDepartTicket();
        var returnTicket = route.getReturnTicket();
        return (PRICE_WEIGHT * departTicket.getValue()) + (PRICE_WEIGHT * returnTicket.getValue());
    }

    private static int numberOfChangesWeight(Route route) {
        var departTicket = route.getDepartTicket();
        var returnTicket = route.getReturnTicket();
        return (NUMBER_OF_CHANGES_WEIGHT * departTicket.getNumberOfChanges()) + (NUMBER_OF_CHANGES_WEIGHT * returnTicket.getNumberOfChanges());
    }

}
