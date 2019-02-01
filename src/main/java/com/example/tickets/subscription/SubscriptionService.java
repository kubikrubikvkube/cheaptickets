package com.example.tickets.subscription;

import com.google.common.collect.Multimap;

import java.util.List;

public interface SubscriptionService {
    List<Subscription> add(String owner, String origin, String destination);

    List<Subscription> add(String owner, String origin, String destination, String departDate, String returnDate);

    List<Subscription> get(String origin, String destination);

    List<Subscription> get(String owner, String origin, String destination);

    List<Subscription> get(String ownerEmail);

    void delete(String owner, String origin, String destination);

    void delete(String owner);

    void delete(Long id);

    Multimap<String, String> findDistinctOriginAndDestination();

    List<Subscription> findAll();

    long count();

    Subscription save(SubscriptionDTO subscriptionDTO);

    Subscription save(Subscription subscriptionDTO);
}
