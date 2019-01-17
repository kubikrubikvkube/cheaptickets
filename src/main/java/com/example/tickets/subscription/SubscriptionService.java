package com.example.tickets.subscription;

import com.google.common.collect.Multimap;

import java.util.List;

public interface SubscriptionService {
    List<Subscription> add(String owner, String origin, String destination);

    List<Subscription> get(String owner, String origin, String destination);

    List<Subscription> get(String owner);

    void delete(String owner, String origin, String destination);

    void delete(String owner);

    List<Subscription> add(String owner, String origin, String destination, String tripDurationInDays);

    Multimap<String, String> findDistinctOriginAndDestination();
}
