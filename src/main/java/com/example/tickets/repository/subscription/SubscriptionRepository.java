package com.example.tickets.repository.subscription;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Repository
@Transactional
public interface SubscriptionRepository extends CrudRepository<Subscription, Long> {
    List<Subscription> findByOwner(String owner);

    List<Subscription> findByOrigin(String origin);

    List<Subscription> findByDestination(String destination);

    List<Subscription> findByOriginAndDestination(String origin, String destination);

    List<Subscription> findByOwnerAndOriginAndDestination(String owner, String origin, String destination);

    List<Subscription> findByOwnerAndOriginAndDestinationAndDepartDateAndReturnDate(String owner, String origin, String destination, Date departDate, Date returnDate);

    List<Subscription> findByOwnerAndOriginAndDestinationAndDepartDateAndReturnDateAndExpirationDate(String owner, String origin, String destination, Date departDate, Date returnDate, Date expirationDate);

    boolean existsByOwnerAndOriginAndDestination(String owner, String origin, String destination);

    boolean existsByOwnerAndOriginAndDestinationAndDepartDateAndReturnDate(String owner, String origin, String destination, Date departDate, Date returnDate);

    boolean existsByOwnerAndOriginAndDestinationAndDepartDateAndReturnDateAndExpirationDate(String owner, String origin, String destination, Date departDate, Date returnDate, Date expirationDate);
}
