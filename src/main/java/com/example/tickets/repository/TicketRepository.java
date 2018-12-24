package com.example.tickets.repository;

import com.example.tickets.ticket.TicketEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Repository
@Transactional
public interface TicketRepository extends CrudRepository<TicketEntity, Long> {
    List<TicketEntity> findByDepartDate(Date date);
}
