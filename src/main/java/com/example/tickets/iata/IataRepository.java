package com.example.tickets.iata;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface IataRepository extends JpaRepository<Iata, Long> {

    @Query("select i from Iata i where i.code = ?1")
    List<Iata> findByCode(String code);

    @Query("select i from Iata i where i.place = ?1")
    Iata findByPlace(String place);

    @Query("select i from Iata i where i.code = ?1 and i.isCanonical = true")
    Iata findByCodeCanonical(String code);

    @Query("select i from Iata i where i.place = ?1 and i.isCanonical = true")
    Iata findByPlaceCanonical(String place);

    boolean existsByPlace(String place);

    boolean existsByCode(String code);

    @Query("select count(i)>0 from Iata i where i.code = ?1 and i.isCanonical = true")
    boolean existsByCodeCanonical(String code);

    @Query("select count(i)>0 from Iata i where i.place = ?1 and i.isCanonical = true")
    boolean existsByPlaceCanonical(String place);

}

