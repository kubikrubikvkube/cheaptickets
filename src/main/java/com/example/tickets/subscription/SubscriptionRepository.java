package com.example.tickets.subscription;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

@Repository
@Transactional
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    @Query("select s from Subscription s where s.owner.email = ?1")
    List<Subscription> findByOwnerEmail(String ownerEmail);

    @Query("select s from Subscription s where s.origin = ?1")
    List<Subscription> findByOrigin(String origin);

    @Query("select s from Subscription s where s.destination = ?1")
    List<Subscription> findByDestination(String destination);

    @Query("select s from Subscription s where s.origin = ?1 and s.destination = ?2")
    List<Subscription> findBy(String origin, String destination);


    @Query("select s from Subscription s where s.owner.email = ?1 and s.origin = ?2 and s.destination = ?3")
    List<Subscription> findBy(String ownerEmail, String origin, String destination);

    @Query("select s from Subscription s where s.owner.email = ?1 and s.origin = ?2 and s.destination = ?3 and s.departDate = ?4 and s.returnDate= ?5")
    List<Subscription> findBy(String ownerEmail, String origin, String destination, LocalDate departDate, LocalDate returnDate);

    @Query("select count(s)>0 from Subscription s where s.owner.email = ?1 and s.origin = ?2 and s.destination = ?3")
    boolean exists(String ownerEmail, String origin, String destination);

    @Query("select count(s)>0 from Subscription s where s.owner.email = ?1 and s.origin = ?2 and s.destination = ?3 and s.departDate = ?4 and s.returnDate = ?5")
    boolean exists(String ownerEmail, String origin, String destination, LocalDate departDate, LocalDate returnDate);

    @Query(value = "select distinct q.origin,q.destination from Subscription q", nativeQuery = true)
    List<Object[]> findDistinctOriginAndDestination();
}
