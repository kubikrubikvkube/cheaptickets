package com.example.tickets.iata;

import com.example.tickets.subscription.Subscription;
import com.example.tickets.subscription.SubscriptionDTO;

public interface IATAResolver {

    String resolve(String place);

    SubscriptionDTO resolve(SubscriptionDTO dto);

    Subscription resolve(Subscription s);
}
