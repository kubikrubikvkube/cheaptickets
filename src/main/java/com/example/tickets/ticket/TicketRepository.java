package com.example.tickets.ticket;

import com.example.tickets.subscription.Subscription;
import com.example.tickets.subscription.SubscriptionDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

@Repository
@Transactional
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByDepartDate(LocalDate date);

    Long countByOriginAndDestinationAndDepartDate(String origin, String destination, LocalDate departDate);

    List<Ticket> findByOriginAndDestinationAndDepartDateAndValue(String origin, String destination, LocalDate departDate, Integer value);

    List<Ticket> findByOriginAndDestinationAndDepartDate(String origin, String destination, LocalDate departDate);

    @Query("select DISTINCT(t.origin) from Ticket t")
    List<String> findDistinctOrigins();

    @Query("select DISTINCT(t.destination) from Ticket t")
    List<String> findDistinctDestinations();

    List<Ticket> findByOriginAndDestination(String origin, String destination);

    @Query("select t from Ticket t where t.origin = :#{#subscriptionDto.origin} and t.destination = :#{#subscriptionDto.destination}")
    List<Ticket> findBySubscription(SubscriptionDto subscriptionDto);

    @Query("select t from Ticket t where t.origin = :#{#subscription.origin} and t.destination = :#{#subscription.destination}")
    List<Ticket> findBySubscription(Subscription subscription);

    boolean existsByOriginAndDestinationAndDepartDateAndValue(String origin, String destination, LocalDate departDate, Integer value);

    boolean existsByOriginAndDestinationAndDepartDate(String origin, String destination, LocalDate departDate);

    boolean existsByOriginAndDestination(String origin, String destination);

    @Query("select t from Ticket t where t.departDate < ?1")
    List<Ticket> findTicketsInPast(LocalDate startingDate);

    @Query("select t from Ticket t where (t.isExpired is not null and t.isExpired = false) ")
    List<Ticket> findNonExpiredTickets();

    @Query("select count(t) from Ticket t where (t.isExpired is not null and t.isExpired = false)")
    long findNonExpiredTicketsSize();


    @Query("select t from Ticket t where t.isExpired is null")
    List<Ticket> findTicketsWithUnknownExpirationStatus();
}
