package com.example.tickets.ticket;

import com.example.tickets.subscription.SubscriptionDTO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface TicketRepository extends CrudRepository<Ticket, Long> {
    List<Ticket> findByDepartDate(OffsetDateTime date);

    List<Ticket> findByOriginAndDestinationAndDepartDateAndValue(String origin, String destination, OffsetDateTime departDate, Integer value);

    List<Ticket> findByOriginAndDestinationAndDepartDate(String origin, String destination, OffsetDateTime departDate);

    List<Ticket> findByOriginAndDestination(String origin, String destination);

    @Query("select t from Ticket t where t.origin = :#{#subscriptionDTO.origin} and t.destination = :#{#subscriptionDTO.destination}")
    List<Ticket> findBySubscription(SubscriptionDTO subscriptionDTO);

    boolean existsByOriginAndDestinationAndDepartDateAndValue(String origin, String destination, OffsetDateTime departDate, Integer value);

    @Query("select t from Ticket t where t.origin = ?1 and t.destination = ?2 and t.departDate = ?3")
    Optional<Ticket> findCheapestForDate(String origin, String destination, OffsetDateTime departDate);

    boolean existsByOriginAndDestinationAndDepartDate(String origin, String destination, OffsetDateTime departDate);

    boolean existsByOriginAndDestination(String origin, String destination);

    @Query("select t from Ticket t where (t.departDate < ?1 or t.expiresAt < ?1) and (t.isExpired = ?2 or t.isExpired is null)")
    List<Ticket> findExpiredTickets(OffsetDateTime dateTime, boolean markedAsExpired);
}
