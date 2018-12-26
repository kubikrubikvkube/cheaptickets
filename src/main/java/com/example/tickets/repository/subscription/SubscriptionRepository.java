package com.example.tickets.repository.subscription;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface SubscriptionRepository extends CrudRepository<Subscription, Long> {
    List<Subscription> findByOwner(String owner);

    List<Subscription> findByOrigin(String origin);

    List<Subscription> findByDestination(String destination);

    List<Subscription> findByOriginAndDestination(String origin, String destination);
}
