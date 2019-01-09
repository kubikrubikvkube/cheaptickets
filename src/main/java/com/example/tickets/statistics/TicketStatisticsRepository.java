package com.example.tickets.statistics;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TicketStatisticsRepository extends CrudRepository<TicketStatistics, Long> {

    @Query("select s from TicketStatistics s where s.origin = ?1 and s.destination = ?2")
    Optional<TicketStatistics> findByOriginAndDestination(String origin, String destination);

    @Query(value = "SELECT MIN(value) FROM ticket t WHERE t.origin = ?1 AND t.destination = ?2 AND t.depart_date = ?3", nativeQuery = true)
    Double calculateMinTicketPriceForDate(String origin, String destination, LocalDate date);

    @Query(value = "SELECT AVG(value) FROM ticket t WHERE t.origin = ?1 AND t.destination = ?2 AND t.depart_date = ?3", nativeQuery = true)
    Double calculateAvgTicketPriceForDate(String origin, String destination, LocalDate date);

    @Query(value = "SELECT value FROM ticket t WHERE t.origin = ?1 AND t.destination = ?2 AND t.depart_date = ?3", nativeQuery = true)
    List<Double> calculateTicketPrices(String origin, String destination, LocalDate date);

    @Query(value = "SELECT  percentile_cont(0.05) within group (order by value asc) FROM ticket t WHERE t.origin = ?1 AND t.destination = ?2 AND t.depart_date = ?3", nativeQuery = true)
    Double calculate5PercentileTicketPriceForDate(String origin, String destination, LocalDate date);
}
