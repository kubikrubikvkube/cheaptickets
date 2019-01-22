package com.example.tickets.statistics;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
@Transactional
public interface TicketStatisticsRepository extends JpaRepository<TicketStatistics, Long> {
    @Query("select s from TicketStatistics s where s.origin = ?1 and s.destination = ?2")
    Optional<TicketStatistics> findByOriginAndDestination(String origin, String destination);
}
