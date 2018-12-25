package com.example.tickets.repository.subscription;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface SubscriptionRepository extends CrudRepository<SubscriptionEntity, Long> {
    List<SubscriptionEntity> findByOwner(String owner);

    List<SubscriptionEntity> findByOrigin(String origin);

    List<SubscriptionEntity> findByDestination(String destination);

    List<SubscriptionEntity> findByOriginAndDestination(String origin, String destination);
}
