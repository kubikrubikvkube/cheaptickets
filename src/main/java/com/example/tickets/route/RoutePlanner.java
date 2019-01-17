package com.example.tickets.route;

import com.example.tickets.subscription.Subscription;

public interface RoutePlanner {
    Route plan(Subscription subscription);

}
