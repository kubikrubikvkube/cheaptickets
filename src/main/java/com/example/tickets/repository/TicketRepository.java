package com.example.tickets.repository;

import com.example.tickets.service.subscription.SubscriptionDTO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Repository
@Transactional
public interface TicketRepository extends CrudRepository<Ticket, Long> {
    List<Ticket> findByDepartDate(Date date);

    List<Ticket> findByOriginAndDestinationAndDepartDateAndValue(String origin, String destination, Date departDate, Integer value);

    List<Ticket> findByOriginAndDestinationAndDepartDate(String origin, String destination, Date departDate);

    List<Ticket> findByOriginAndDestination(String origin, String destination);

    @Query("select t from Ticket t where t.origin = :#{#subscriptionDTO.origin} and t.destination = :#{#subscriptionDTO.destination}")
    List<Ticket> findBySubscription(SubscriptionDTO subscriptionDTO);

    boolean existsByOriginAndDestinationAndDepartDateAndValue(String origin, String destination, Date departDate, Integer value);

    @Query("select t from Ticket t where t.origin = ?1 and t.destination = ?2 and t.departDate = ?3")
    Ticket findCheapestForDate(String origin, String destination, Date departDate);

    boolean existsByOriginAndDestinationAndDepartDate(String origin, String destination, Date departDate);

    boolean existsByOriginAndDestination(String origin, String destination);
}
