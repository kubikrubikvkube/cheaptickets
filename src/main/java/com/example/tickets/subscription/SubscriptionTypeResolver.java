package com.example.tickets.subscription;

public interface SubscriptionTypeResolver {
    SubscriptionType resolve(Subscription subscription);

    SubscriptionType resolve(SubscriptionDto subscription);
}
