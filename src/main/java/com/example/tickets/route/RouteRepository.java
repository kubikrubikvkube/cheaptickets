package com.example.tickets.route;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface RouteRepository extends CrudRepository<Route, Long> {
}
