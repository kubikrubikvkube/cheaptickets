package com.example.tickets.subscription;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Repository
@Transactional
public interface SubscriptionRepository extends CrudRepository<Subscription, Long> {

    @Query("select s from Subscription s where s.owner = ?1")
    List<Subscription> findByOwner(String owner);

    @Query("select s from Subscription s where (s.isExpired = false or s.isExpired is null)")
    List<Subscription> findNonExpired();

    @Query("select s from Subscription s where s.origin = ?1")
    List<Subscription> findByOrigin(String origin);

    @Query("select s from Subscription s where s.destination = ?1")
    List<Subscription> findByDestination(String destination);

    @Query("select s from Subscription s where s.origin = ?1 and s.destination = ?2")
    List<Subscription> findBy(String origin, String destination);

    @Query("select s from Subscription s where s.owner = ?1 and s.origin = ?2 and s.destination = ?3")
    List<Subscription> findBy(String owner, String origin, String destination);

    @Query("select s from Subscription s where s.owner = ?1 and s.origin = ?2 and s.destination = ?3 and s.departDate = ?4 and s.returnDate= ?5")
    List<Subscription> findBy(String owner, String origin, String destination, Date departDate, Date returnDate);

    @Query("select s from Subscription s where s.owner = ?1 and s.origin = ?2 and s.destination = ?3 and s.departDate = ?4 and s.returnDate= ?5 and s.expirationDate = ?6")
    List<Subscription> findBy(String owner, String origin, String destination, Date departDate, Date returnDate, Date expirationDate);

    @Query("select count(s)>0 from Subscription s where s.owner = ?1 and s.origin = ?2 and s.destination = ?3")
    boolean exists(String owner, String origin, String destination);


    @Query("select count(s)>0 from Subscription s where s.owner = ?1 and s.origin = ?2 and s.destination = ?3 and s.departDate = ?4 and s.returnDate = ?5")
    boolean exists(String owner, String origin, String destination, Date departDate, Date returnDate);

    @Query("select count(s)>0 from Subscription s where s.owner = ?1 and s.origin = ?2 and s.destination = ?3 and s.departDate = ?4 and s.returnDate = ?5 and s.expirationDate = ?6")
    boolean exists(String owner, String origin, String destination, Date departDate, Date returnDate, Date expirationDate);
}
