package com.example.tickets.route;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface RouteRepository extends JpaRepository<Route, Long> {

    @Query("select r from Route r where r.origin = ?1 and r.destination = ?2")
    List<Route> findBy(String origin, String destination);
}
