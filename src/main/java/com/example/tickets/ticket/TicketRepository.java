package com.example.tickets.ticket;

import com.example.tickets.subscription.Subscription;
import com.example.tickets.subscription.SubscriptionDTO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

@Repository
@Transactional
public interface TicketRepository extends CrudRepository<Ticket, Long> {
    List<Ticket> findByDepartDate(LocalDate date);

    Long countByOriginAndDestinationAndDepartDate(String origin, String destination, LocalDate departDate);

    List<Ticket> findByOriginAndDestinationAndDepartDateAndValue(String origin, String destination, LocalDate departDate, Integer value);

    List<Ticket> findByOriginAndDestinationAndDepartDate(String origin, String destination, LocalDate departDate);

    List<Ticket> findByOriginAndDestination(String origin, String destination);

    @Query("select t from Ticket t where t.origin = :#{#subscriptionDTO.origin} and t.destination = :#{#subscriptionDTO.destination}")
    List<Ticket> findBySubscription(SubscriptionDTO subscriptionDTO);

    @Query("select t from Ticket t where t.origin = :#{#subscription.origin} and t.destination = :#{#subscription.destination}")
    List<Ticket> findBySubscription(Subscription subscription);

    boolean existsByOriginAndDestinationAndDepartDateAndValue(String origin, String destination, LocalDate departDate, Integer value);

    boolean existsByOriginAndDestinationAndDepartDate(String origin, String destination, LocalDate departDate);

    boolean existsByOriginAndDestination(String origin, String destination);

    @Query("select t from Ticket t where (t.departDate < ?1 or t.expiresAt < ?1) and t.isExpired = ?2")
    List<Ticket> findExpiredTickets(LocalDate departDate, boolean markedAsExpired);

    @Query("select t from Ticket t where t.isExpired is null")
    List<Ticket> findTicketsWithUnknownExpirationStatus();
}
