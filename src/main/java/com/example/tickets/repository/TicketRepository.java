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

    @Query("select t from Ticket t where t.origin = :origin and t.destination = :destination and t.departDate = :departDate and t.value = :value")
    List<Ticket> findByBasicData(String origin, String destination, Date departDate, Integer value);

    @Query("select t from Ticket t where t.origin = :#{#subscriptionDTO.origin} and t.destination = :#{#subscriptionDTO.destination}")
    List<Ticket> findBySubscription(SubscriptionDTO subscriptionDTO);

    @Query("select count(t)>0 from Ticket t where t.origin = :origin and t.destination = :destination and t.departDate = :departDate and t.value = :value")
    boolean existsByBasicData(String origin, String destination, Date departDate, Integer value);
}
