package com.example.tickets.statistics;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface TicketStatisticsRepository extends CrudRepository<TicketStatistics, Long> {

    @Query("select s from TicketStatistics s where s.origin = ?1 and s.destination = ?2")
    Optional<TicketStatistics> findByOriginAndDestination(String origin, String destination);
}
