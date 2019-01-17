package com.example.tickets.ticket;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface CheapTicketRepository extends CrudRepository<CheapTicket, Long> {
}
