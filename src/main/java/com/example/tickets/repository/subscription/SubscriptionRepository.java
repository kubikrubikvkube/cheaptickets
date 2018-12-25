package com.example.tickets.repository.subscription;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SubscriptionRepository extends CrudRepository<SubscriptionEntity, Long> {
    List<SubscriptionEntity> findByOwner(String owner);

    List<SubscriptionEntity> findByOrigin(String origin);

    List<SubscriptionEntity> findByDestination(String destination);

    List<SubscriptionEntity> findByOriginAndDestination(String origin, String destination);
}
