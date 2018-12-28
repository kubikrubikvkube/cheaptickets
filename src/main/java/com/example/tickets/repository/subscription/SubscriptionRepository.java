package com.example.tickets.repository.subscription;

import org.springframework.data.jpa.repository.Query;
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

    @Query("select count(s)>0 from Subscription s where s.owner = ?1 and s.origin = ?2 and s.destination = ?3")
    boolean exists(String owner, String origin, String destination);


    @Query("select count(s)>0 from Subscription s where s.owner = ?1 and s.origin = ?2 and s.destination = ?3 and s.departDate = ?4 and s.returnDate = ?5")
    boolean exists(String owner, String origin, String destination, Date departDate, Date returnDate);

    @Query("select count(s)>0 from Subscription s where s.owner = ?1 and s.origin = ?2 and s.destination = ?3 and s.departDate = ?4 and s.returnDate = ?5 and s.expirationDate = ?6")
    boolean exists(String owner, String origin, String destination, Date departDate, Date returnDate, Date expirationDate);
}
