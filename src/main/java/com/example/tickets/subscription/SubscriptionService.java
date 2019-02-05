package com.example.tickets.subscription;

import com.google.common.collect.Multimap;

import java.util.List;
import java.util.Optional;

public interface SubscriptionService {
    List<Subscription> add(String owner, String origin, String destination);

    List<Subscription> add(String owner, String origin, String destination, String departDate, String returnDate);

    List<Subscription> get(String origin, String destination);

    List<Subscription> get(String owner, String origin, String destination);

    List<Subscription> get(String ownerEmail);

    Optional<Subscription> find(SubscriptionDTO dto);

    Optional<Subscription> find(Long id);

    void delete(String owner, String origin, String destination);

    void delete(String owner);

    void delete(Long id);

    Multimap<String, String> findDistinctOriginAndDestination();

    List<Subscription> findAll();

    long count();

    Subscription save(SubscriptionDTO subscriptionDTO);

    Subscription save(Subscription subscriptionDTO);
}
