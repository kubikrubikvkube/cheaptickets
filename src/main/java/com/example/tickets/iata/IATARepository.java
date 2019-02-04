package com.example.tickets.iata;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface IATARepository extends JpaRepository<IATA, Long> {

    @Query("select i from IATA i where i.code = ?1")
    List<IATA> findByCode(String code);

    @Query("select i from IATA i where i.place = ?1")
    Optional<IATA> findByPlace(String place);

    boolean existsByPlace(String place);

    boolean existsByCode(String code);

}

