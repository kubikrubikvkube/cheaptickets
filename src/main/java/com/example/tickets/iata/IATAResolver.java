package com.example.tickets.iata;

import com.example.tickets.subscription.Subscription;
import com.example.tickets.subscription.SubscriptionDTO;

public interface IATAResolver {
    /**
     * Returns IATA code for this place
     *
     * @param place
     * @return
     */
    String resolve(String place);

    SubscriptionDTO resolve(SubscriptionDTO dto);

    Subscription resolve(Subscription s);
}
