package com.example.tickets.repository;

import com.example.tickets.service.subscription.Subscription;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Repository
@Transactional
public interface TicketRepository extends CrudRepository<TicketEntity, Long> {
    List<TicketEntity> findByDepartDate(Date date);

    @Query("select t from TicketEntity t where t.origin = :origin and t.destination = :destination and t.departDate = :departDate and t.value = :value")
    List<TicketEntity> findByBasicData(String origin, String destination, Date departDate, Integer value);

    @Query("select t from TicketEntity t where t.origin = :#{#subscription.origin} and t.destination = :#{#subscription.destination}")
    List<TicketEntity> findBySubscription(Subscription subscription);
}
