package com.example.tickets.iata;

import com.example.tickets.subscription.Subscription;
import com.example.tickets.subscription.SubscriptionDto;

public interface IataResolver {

    String resolve(String place);

    SubscriptionDto resolve(SubscriptionDto dto);

    Subscription resolve(Subscription s);
}
