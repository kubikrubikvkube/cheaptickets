package com.example.tickets.ticket;

import com.example.tickets.subscription.Subscription;
import com.example.tickets.subscription.SubscriptionDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

@Repository
@Transactional
public interface CheapTicketRepository extends JpaRepository<CheapTicket, Long> {

    List<CheapTicket> findByOriginAndDestinationAndDepartDate(String origin, String destination, LocalDate departDate);

    List<CheapTicket> findByOriginAndDestination(String origin, String destination);

    @Query("select t from CheapTicket t where (t.departDate < ?1 or t.expiresAt < ?1) and t.isExpired = ?2")
    List<CheapTicket> findExpiredTickets(LocalDate departDate, boolean markedAsExpired);

    @Query("select t from CheapTicket t where t.isExpired is null")
    List<CheapTicket> findTicketsWithUnknownExpirationStatus();

    @Query("select t from CheapTicket t where t.origin = :#{#subscriptionDTO.origin} and t.destination = :#{#subscriptionDTO.destination}")
    List<CheapTicket> findBySubscription(SubscriptionDTO subscriptionDTO);

    @Query("select t from CheapTicket t where t.origin = :#{#subscription.origin} and t.destination = :#{#subscription.destination}")
    List<CheapTicket> findBySubscription(Subscription subscription);

}
