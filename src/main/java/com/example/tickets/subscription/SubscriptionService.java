package com.example.tickets.subscription;

import java.util.List;

public interface SubscriptionService {
    Subscription add(String owner, String origin, String destination);

    Subscription get(String owner, String origin, String destination);

    List<Subscription> get(String owner);

    void delete(String owner, String origin, String destination);

    void delete(String owner);
}
