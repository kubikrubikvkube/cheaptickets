package com.example.tickets.repository;

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

    @Query("select t from TicketEntity t where t.origin = ?1 and t.destination = ?2 and t.departDate = ?3 and t.value = ?4")
    List<TicketEntity> findByBasicData(String origin, String destination, Date departDate, Integer value);
}
